package com.example.ritesh.carboncollector.postgres.model;

import com.example.ritesh.carboncollector.postgres.enumeration.SensorStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class SensorStats implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID sensorid;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SensorStatus status;

    //for capturing concurrent normal co2 level after Alert state
    private Integer normalStateCount = 0;

    //for capturing concurrent above normal co2 level after Alert state
    private Integer warnStateCount = 0;

    public SensorStats(UUID sensorid, SensorStatus status, Integer normalStateCount, Integer warnStateCount) {
        this.sensorid = sensorid;
        this.status = status;
        this.normalStateCount = normalStateCount;
        this.warnStateCount = warnStateCount;
    }
}
