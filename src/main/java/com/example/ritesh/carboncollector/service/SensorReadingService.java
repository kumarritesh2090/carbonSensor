package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.SensorReading;
import com.example.ritesh.carboncollector.mongo.model.SensorReadingMessage;
import com.example.ritesh.carboncollector.mongo.repository.SensorReadingRepository;
import com.example.ritesh.carboncollector.restweb.request.CarbonReadingVo;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Log4j
public class SensorReadingService {

    private static final String FAILED_TO_RECORD_NEW_READING_FOR_SENSOR = "failed to record new reading for sensor:{}";

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Transactional
    public void captureReading(CarbonReadingVo carbonReadingVo, UUID sensorId) {
        try {
            sensorReadingRepository.save(new SensorReading(UUID.randomUUID(), sensorId, carbonReadingVo.getCo2(), carbonReadingVo.getTime()));
            kafkaProducerService.publishReadings(new SensorReadingMessage(sensorId, carbonReadingVo.getCo2(), carbonReadingVo.getTime()));
        } catch (Exception e) {
            log.error(new StringFormattedMessage(FAILED_TO_RECORD_NEW_READING_FOR_SENSOR, sensorId), e);
        }

    }
}
