package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.Sensor;
import com.example.ritesh.carboncollector.mongo.repository.SensorReadingRepository;
import com.example.ritesh.carboncollector.mongo.repository.SensorRepository;
import com.example.ritesh.carboncollector.restweb.response.SensorMetricsResponse;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @InjectMocks
    private SensorService sensorService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getSensorMetrics() {
        Sensor sensor = new Sensor(UUID.randomUUID(), 2000f, 2200);
        Optional<Sensor> sensorOptional = Optional.ofNullable(sensor);
        Mockito.when(sensorRepository.findById(Mockito.any(UUID.class))).thenReturn(sensorOptional);
        SensorMetricsResponse sensorMetricsResponse = sensorService.getSensorMetrics(UUID.randomUUID());
        Assert.assertEquals(sensorMetricsResponse.getAverageCo2Level(), sensor.getAverageCo2Level());
        Assert.assertEquals(sensorMetricsResponse.getMaxCo2Level(), sensor.getMaxCo2Level());
        verify(sensorRepository).findById(Mockito.any(UUID.class));
    }

    @Test
    public void getSensorMetricsEmpty() {
        Sensor sensor = null;
        Optional<Sensor> sensorOptional = Optional.ofNullable(sensor);
        Mockito.when(sensorRepository.findById(Mockito.any(UUID.class))).thenReturn(sensorOptional);
        try {
            SensorMetricsResponse sensorMetricsResponse = sensorService.getSensorMetrics(UUID.randomUUID());
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), SensorService.FAILED_TO_FETCH_SENSOR_METRICS_PLEASE_RETRY);
        }
        verify(sensorRepository).findById(Mockito.any(UUID.class));
    }

    @Test
    public void populateSensorStatLevels() {
        List<Sensor> sensors = new ArrayList<>();
        Sensor sensor = new Sensor(UUID.randomUUID(), 2000f, 2200);
        sensors.add(sensor);
        AggregationResults<Sensor> aggregationResults = new AggregationResults<>(sensors, new Document());
        Mockito.when(sensorReadingRepository.getNewSensorStats(Mockito.anyString(), Mockito.anyString())).thenReturn(aggregationResults);
        Mockito.when(sensorRepository.saveAll(Mockito.anyIterable())).thenReturn(null);
        sensorService.populateSensorStatLevels();
        verify(sensorReadingRepository).getNewSensorStats(Mockito.anyString(), Mockito.anyString());
        verify(sensorRepository).saveAll(Mockito.anyIterable());
    }

    @Test(expected = RuntimeException.class)
    public void populateSensorStatLevelsException() {
        List<Sensor> sensors = new ArrayList<>();
        Sensor sensor = new Sensor(UUID.randomUUID(), 2000f, 2200);
        sensors.add(sensor);
        AggregationResults<Sensor> aggregationResults = new AggregationResults<>(sensors, new Document());
        Mockito.when(sensorReadingRepository.getNewSensorStats(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException());
        try {
            sensorService.populateSensorStatLevels();
        } catch (Exception e) {
            verify(sensorReadingRepository).getNewSensorStats(Mockito.anyString(), Mockito.anyString());
            throw e;
        }
    }
}
