package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.SensorReadingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Log4j
public class NewReadingsKafkaConsumer {

    private static final String PROCESSING_NEW_READINGS_MESSAGE_TO_SET_REQUIRED_STATUS_LEVEL_FOR_SENSOR_ID_FOR_CO_2_LEVEL = "Processing new readings message to set required status level for sensorId:{} for co2Level:{}";

    @Autowired
    private SensorStatsService sensorStatsService;

    @KafkaListener(topics = "${carbonsensor.readings.topic}",
            groupId = "carboncollector-group-id")
    @KafkaHandler
    public void receiveNewSensorReading(@Payload Object payload,
                                        @Headers MessageHeaders headers) {
        SensorReadingMessage data = new ObjectMapper().convertValue(((ConsumerRecord) payload).value(), SensorReadingMessage.class);
        log.info(new StringFormattedMessage(PROCESSING_NEW_READINGS_MESSAGE_TO_SET_REQUIRED_STATUS_LEVEL_FOR_SENSOR_ID_FOR_CO_2_LEVEL, data.getSensorId(), data.getCo2()));
        sensorStatsService.processNewSensorReading(data);
    }

}
