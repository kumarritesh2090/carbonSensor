package com.example.ritesh.carboncollector.postgres.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Alerts implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UUID sensorId;

    @Column(nullable = false)
    private Integer co2Level;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    public Alerts(UUID sensorId, Integer co2Level, Date startTime, Date endTime) {
        this.sensorId = sensorId;
        this.co2Level = co2Level;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
