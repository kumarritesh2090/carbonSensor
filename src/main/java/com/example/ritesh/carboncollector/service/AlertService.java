package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.postgres.model.Alerts;
import com.example.ritesh.carboncollector.postgres.repository.AlertRepository;
import com.example.ritesh.carboncollector.restweb.response.SensorAlertsResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j
public class AlertService {

    public static final String FAILED_TO_FETCH_SENSOR_ALERTS_PLEASE_RETRY = "Failed to fetch sensor alerts, please retry";
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    @Autowired
    private AlertRepository alertRepository;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SensorAlertsResponse getAlerts(UUID sensorId) {
        try {
            List<Alerts> alerts = alertRepository.findAlertsBySensorId(sensorId);
            if (!alerts.isEmpty()) {
                List<Integer> measurements = alerts.stream().map(Alerts::getCo2Level).collect(Collectors.toList());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_FORMAT);
                String alertEndTime = null == alerts.get(0).getEndTime() ? "" : simpleDateFormat.format(alerts.get(0).getEndTime());
                return new SensorAlertsResponse(simpleDateFormat.format(alerts.get(0).getStartTime()), alertEndTime, measurements);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(FAILED_TO_FETCH_SENSOR_ALERTS_PLEASE_RETRY, e);

        }
    }
}
