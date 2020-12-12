package com.example.ritesh.carboncollector.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorReadingMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID sensorId;

    private Integer co2;

    private String time;

}
