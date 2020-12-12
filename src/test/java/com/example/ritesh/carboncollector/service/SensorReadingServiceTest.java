package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.SensorReading;
import com.example.ritesh.carboncollector.mongo.model.SensorReadingMessage;
import com.example.ritesh.carboncollector.mongo.repository.SensorReadingRepository;
import com.example.ritesh.carboncollector.restweb.request.CarbonReadingVo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class SensorReadingServiceTest {

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private SensorReadingService sensorReadingService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void captureReading() {
        Mockito.when(sensorReadingRepository.save(Mockito.any(SensorReading.class))).thenReturn(null);
        Mockito.doNothing().when(kafkaProducerService).publishReadings(Mockito.any(SensorReadingMessage.class));
        sensorReadingService.captureReading(new CarbonReadingVo(2000, LocalDate.now().toString()), UUID.randomUUID());
        verify(sensorReadingRepository).save(Mockito.any(SensorReading.class));
        verify(kafkaProducerService).publishReadings(Mockito.any(SensorReadingMessage.class));
    }

    @Test
    public void captureReadingException() {
        Mockito.when(sensorReadingRepository.save(Mockito.any(SensorReading.class))).thenThrow(new RuntimeException());
        sensorReadingService.captureReading(new CarbonReadingVo(2000, LocalDate.now().toString()), UUID.randomUUID());
        verify(sensorReadingRepository).save(Mockito.any(SensorReading.class));
        verifyNoInteractions(kafkaProducerService);
    }
}
