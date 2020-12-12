package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.Sensor;
import com.example.ritesh.carboncollector.mongo.repository.SensorReadingRepository;
import com.example.ritesh.carboncollector.mongo.repository.SensorRepository;
import com.example.ritesh.carboncollector.restweb.response.SensorMetricsResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j
public class SensorService {

    public static final String FAILED_TO_FETCH_SENSOR_METRICS_PLEASE_RETRY = "Failed to fetch sensor metrics, please retry";
    private static final String ISSUE_IN_CALCULATING_CARBON_STAT_LEVEL_FOR_SENSORS = "Issue in calculating  carbon stat level for sensors";

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SensorMetricsResponse getSensorMetrics(UUID sensorId) {
        try {
            Optional<Sensor> sensorOptional = sensorRepository.findById(sensorId);
            if (sensorOptional.isPresent()) {
                return new SensorMetricsResponse(sensorOptional.get().getAverageCo2Level(), sensorOptional.get().getMaxCo2Level());
            }
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException(FAILED_TO_FETCH_SENSOR_METRICS_PLEASE_RETRY, e.getCause());
        }
    }

    @Transactional
    public void populateSensorStatLevels() {
        try {
            LocalDate today = LocalDate.now();
            AggregationResults<Sensor> sensors = sensorReadingRepository.getNewSensorStats(today.plusMonths(-1).toString(), today.toString());
            if (!sensors.getMappedResults().isEmpty()) {
                sensorRepository.saveAll(sensors.getMappedResults());
            }
        } catch (Exception e) {
            log.error(ISSUE_IN_CALCULATING_CARBON_STAT_LEVEL_FOR_SENSORS, e);
            throw new RuntimeException();
        }
    }

}
