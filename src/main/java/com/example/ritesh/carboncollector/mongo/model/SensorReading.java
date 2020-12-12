package com.example.ritesh.carboncollector.mongo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SensorReading {

    @Id
    private UUID id;

    @Field
    @Indexed
    private UUID sensorId;

    @Field
    private Integer co2;

    @Field
    private String time;

    public SensorReading(UUID sensorId, Integer co2, String time) {
        this.sensorId = sensorId;
        this.co2 = co2;
        this.time = time;
    }

    public SensorReading(UUID id, UUID sensorId, Integer co2, String time) {
        this.id = id;
        this.sensorId = sensorId;
        this.co2 = co2;
        this.time = time;
    }
}
