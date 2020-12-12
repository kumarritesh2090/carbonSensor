package com.example.ritesh.carboncollector.restweb.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarbonReadingVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer co2;

    @NotEmpty
    private String time;

}
