package com.example.ritesh.carboncollector.restweb.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorStatusResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "status")
    private String status;

}
