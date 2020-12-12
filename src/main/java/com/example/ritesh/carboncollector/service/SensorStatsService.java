package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.SensorReadingMessage;
import com.example.ritesh.carboncollector.postgres.enumeration.SensorStatus;
import com.example.ritesh.carboncollector.postgres.model.Alerts;
import com.example.ritesh.carboncollector.postgres.model.SensorStats;
import com.example.ritesh.carboncollector.postgres.repository.AlertRepository;
import com.example.ritesh.carboncollector.postgres.repository.SensorStatsRepository;
import com.example.ritesh.carboncollector.restweb.response.SensorStatusResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Log4j
public class SensorStatsService {

    private static final String FAILED_TO_FETCH_SENSOR_STATUS_PLEASE_RETRY = "Failed to fetch sensor status, please retry";

    @Value("${threshould-level}")
    private Integer threshouldLevel;

    @Value("${threshould-ConsecutiveCount}")
    private Integer threshouldConsecutiveCount;


    @Autowired
    private SensorStatsRepository sensorStatsRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SensorStatusResponse getStatus(UUID sensorId) {
        try {
            SensorStats sensorStats = sensorStatsRepository.findBySensorid(sensorId);
            if (null != sensorStats) {
                return new SensorStatusResponse(sensorStats.getStatus().name());
            }
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException(FAILED_TO_FETCH_SENSOR_STATUS_PLEASE_RETRY, e.getCause());
        }
    }

    /**
     * This method helps process the incoming sensors readings and set appropriate alerts and co2level stats for sensors
     * <p>
     * LOGIC:
     * check if status in "ALERT" state
     * -if yes-> check the message ppm values
     * -if (values< x) -> if (normalStateCount<threshouldConsecutiveCount -1)-> normalStateCount++
     * if (normalStateCount>=threshouldConsecutiveCount -1)-> normalStateCount=0, status=OK
     * -else -> just insert in alert table new entry
     * -if no-> check the message ppm values
     * -if (values> x) ->  status=WARN
     * -if (values< x) -> do nothing
     * -if status=WARN->check the message ppm values
     * -if (values> x) -> if (warnStateCount<threshouldConsecutiveCount -1)-> warnStateCount++
     * if (warnStateCount>=threshouldConsecutiveCount -1)-> warnStateCount=0, status=Alert, insert in alert table new entry
     * -if (values< x) -> set status=OK
     */
    @Transactional
    public void processNewSensorReading(SensorReadingMessage data) {
        try {
            SensorStats sensorStats = sensorStatsRepository.findBySensorid(data.getSensorId());
            if (null != sensorStats) {
                switch (sensorStats.getStatus()) {
                    case OK:
                        if (data.getCo2() >= threshouldLevel) {
                            sensorStats.setStatus(SensorStatus.WARN);
                        }
                        break;
                    case WARN:
                        if (data.getCo2() >= threshouldLevel) {
                            if (sensorStats.getWarnStateCount() < (threshouldConsecutiveCount - 1))
                                sensorStats.setWarnStateCount(sensorStats.getWarnStateCount() + 1);
                            else if (sensorStats.getWarnStateCount() >= (threshouldConsecutiveCount - 1)) {
                                sensorStats.setWarnStateCount(0);
                                sensorStats.setStatus(SensorStatus.ALERT);
                                //Removing old Alerts for storing latest alerts stack
                                alertRepository.deleteAlertsBySensorId(data.getSensorId());
                                //Store new alert
                                alertRepository.save(new Alerts(data.getSensorId(), data.getCo2(), Date.from(ZonedDateTime.parse(data.getTime()).toInstant()), null));
                            }
                        } else {
                            sensorStats.setStatus(SensorStatus.OK);
                        }
                        break;
                    case ALERT:
                        if (data.getCo2() >= threshouldLevel) {
                            //Store new alert
                            alertRepository.save(new Alerts(data.getSensorId(), data.getCo2(), Date.from(ZonedDateTime.parse(data.getTime()).toInstant()), null));
                        } else {
                            if (sensorStats.getNormalStateCount() < (threshouldConsecutiveCount - 1)) {
                                sensorStats.setNormalStateCount(sensorStats.getNormalStateCount() + 1);
                            } else if (sensorStats.getNormalStateCount() >= (threshouldConsecutiveCount - 1)) {
                                sensorStats.setNormalStateCount(0);
                                sensorStats.setStatus(SensorStatus.OK);
                                //update the end time for the current open alerts stack
                                alertRepository.updateAlertsEndTimeBySensorId(data.getSensorId(), Date.from(ZonedDateTime.parse(data.getTime()).toInstant()));
                            }

                        }
                }
            } else {
                SensorStatus sensorStatus = (data.getCo2() >= threshouldLevel) ? SensorStatus.WARN : SensorStatus.OK;
                sensorStats = new SensorStats(data.getSensorId(), sensorStatus, 0, 0);
            }
            sensorStatsRepository.save(sensorStats);
        } catch (Exception e) {
            log.error("Failed to process new message", e);
        }

    }
}
