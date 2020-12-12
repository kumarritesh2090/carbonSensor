package com.example.ritesh.carboncollector.mongo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Sensor {

    @Id
    private UUID sensorId;

    @Field
    private Float averageCo2Level;

    @Field
    private Integer maxCo2Level;

    public Sensor(UUID sensorId, Float averageCo2Level, Integer maxCo2Level) {
        this.sensorId = sensorId;
        this.averageCo2Level = averageCo2Level;
        this.maxCo2Level = maxCo2Level;
    }
}
