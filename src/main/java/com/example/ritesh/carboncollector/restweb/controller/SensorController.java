package com.example.ritesh.carboncollector.restweb.controller;

import com.example.ritesh.carboncollector.restweb.path.ApiPath;
import com.example.ritesh.carboncollector.restweb.request.CarbonReadingVo;
import com.example.ritesh.carboncollector.restweb.response.SensorAlertsResponse;
import com.example.ritesh.carboncollector.restweb.response.SensorMetricsResponse;
import com.example.ritesh.carboncollector.restweb.response.SensorStatusResponse;
import com.example.ritesh.carboncollector.service.*;
import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Log4j
public class SensorController {

    private static final String NEW_READING_FOR_UUID_WITH_REQUEST_BODY = "New reading for uuid:{} with requestBody:{}";
    private static final String SENSOR_METRICS_REQUEST_FOR_SENSOR_ID = "sensor metrics request for sensorId:{}";
    private static final String SENSOR_STATUS_REQUEST_FOR_SENSOR_ID = "sensor status request for sensorId:{}";
    private static final String SENSOR_ALERTS_REQUEST_FOR_SENSOR_ID = "sensor alerts request for sensorId:{}";
    private static final String UUID = "uuid";

    @Autowired
    private SensorReadingService sensorReadingService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private SensorStatsService sensorStatsService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private InitService initService;

    @PostMapping(value = ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.MEASUREMENTS, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity readings(@PathVariable(UUID) UUID uuid, @Validated @RequestBody CarbonReadingVo carbonReadingVo) {
        log.info(new StringFormattedMessage(NEW_READING_FOR_UUID_WITH_REQUEST_BODY, uuid, carbonReadingVo));
        sensorReadingService.captureReading(carbonReadingVo, uuid);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.METRICS, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SensorMetricsResponse> getSensorMetrics(@PathVariable(UUID) UUID uuid) {
        log.info(new StringFormattedMessage(SENSOR_METRICS_REQUEST_FOR_SENSOR_ID, uuid));
        return new ResponseEntity<>(sensorService.getSensorMetrics(uuid), HttpStatus.OK);
    }

    @GetMapping(value = ApiPath.ROOT_WITH_SENSOR_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SensorStatusResponse> getSensorStatus(@PathVariable(UUID) UUID uuid) {
        log.info(new StringFormattedMessage(SENSOR_STATUS_REQUEST_FOR_SENSOR_ID, uuid));
        return new ResponseEntity<>(sensorStatsService.getStatus(uuid), HttpStatus.OK);
    }


    @GetMapping(value = ApiPath.ROOT_WITH_SENSOR_ID + ApiPath.ALERTS, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SensorAlertsResponse> getSensorAlerts(@PathVariable(UUID) UUID uuid) {
        log.info(new StringFormattedMessage(SENSOR_ALERTS_REQUEST_FOR_SENSOR_ID, uuid));
        SensorAlertsResponse sensorAlertsResponse = alertService.getAlerts(uuid);
        return (null == sensorAlertsResponse) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(sensorAlertsResponse, HttpStatus.OK);
    }

    //rest end point for scheduler to run everyday and set sensor avg and max co2 level stats
    @GetMapping(value = ApiPath.ROOT + ApiPath.SENSOR_CO2_LEVEL_JOB)
    ResponseEntity runJob() {
        sensorService.populateSensorStatLevels();
        return new ResponseEntity(HttpStatus.OK);
    }

    //init utility method
    @GetMapping(value = ApiPath.ROOT + ApiPath.INIT)
    ResponseEntity<List<UUID>> init() {
        return new ResponseEntity(initService.init(), HttpStatus.OK);
    }
}
