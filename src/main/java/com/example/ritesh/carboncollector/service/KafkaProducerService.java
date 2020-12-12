package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.SensorReadingMessage;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class KafkaProducerService {

    private static final String PUBLISHING_NEW_READING_FOR_SENSOR_ID = "publishing new reading for sensorId:{}";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${carbonsensor.readings.topic}")
    private String readingsTopic;

    public void publishReadings(SensorReadingMessage payload) {
        log.info(new StringFormattedMessage(PUBLISHING_NEW_READING_FOR_SENSOR_ID, payload.getSensorId()));
        Message<SensorReadingMessage> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, readingsTopic)
                .build();
        kafkaTemplate.send(message);
    }
}
