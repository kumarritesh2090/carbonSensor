package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.postgres.model.Alerts;
import com.example.ritesh.carboncollector.postgres.repository.AlertRepository;
import com.example.ritesh.carboncollector.restweb.response.SensorAlertsResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertService alertService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void findAllAlerts() {
        List<Alerts> alerts = new ArrayList<>();
        Date now = new Date();
        alerts.add(new Alerts(UUID.randomUUID(), 2000, now, now));
        alerts.add(new Alerts(UUID.randomUUID(), 2100, now, now));
        Mockito.when(alertRepository.findAlertsBySensorId(Mockito.any(UUID.class))).thenReturn(alerts);
        SensorAlertsResponse sensorAlertsResponse = alertService.getAlerts(UUID.randomUUID());
        Assert.assertEquals(alerts.size(), sensorAlertsResponse.getMeasurments().size());
        verify(alertRepository).findAlertsBySensorId(Mockito.any(UUID.class));
    }

    @Test
    public void findAlertsWithNoActiveAlert() {
        List<Alerts> alerts = new ArrayList<>();
        Mockito.when(alertRepository.findAlertsBySensorId(Mockito.any(UUID.class))).thenReturn(alerts);
        SensorAlertsResponse sensorAlertsResponse = alertService.getAlerts(UUID.randomUUID());
        Assert.assertTrue(null == sensorAlertsResponse);
        verify(alertRepository).findAlertsBySensorId(Mockito.any(UUID.class));
    }

    @Test
    public void findAlertsException() {
        Mockito.when(alertRepository.findAlertsBySensorId(Mockito.any(UUID.class))).thenThrow(new RuntimeException());
        try {
            SensorAlertsResponse sensorAlertsResponse = alertService.getAlerts(UUID.randomUUID());
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().equalsIgnoreCase(AlertService.FAILED_TO_FETCH_SENSOR_ALERTS_PLEASE_RETRY));
        }
        verify(alertRepository).findAlertsBySensorId(Mockito.any(UUID.class));
    }
}
