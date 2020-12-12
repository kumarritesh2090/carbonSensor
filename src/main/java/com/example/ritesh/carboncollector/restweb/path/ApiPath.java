package com.example.ritesh.carboncollector.restweb.path;

public interface ApiPath {

    String INIT = "/init";

    String ROOT = "/api/v1/sensors/";

    String ROOT_WITH_SENSOR_ID = ROOT + "{uuid}";

    String MEASUREMENTS = "/mesurements";

    String METRICS = "/metrics";

    String ALERTS = "/alerts";

    String SENSOR_CO2_LEVEL_JOB = "/job";


}

