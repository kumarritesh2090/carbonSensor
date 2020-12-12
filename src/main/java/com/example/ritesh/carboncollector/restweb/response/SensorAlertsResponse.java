package com.example.ritesh.carboncollector.restweb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorAlertsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "startTime")
    private String startTime;

    @JsonProperty(value = "endTime")
    private String endTime;

    @JsonProperty(value = "measurements")
    private List<Integer> measurments;

}
