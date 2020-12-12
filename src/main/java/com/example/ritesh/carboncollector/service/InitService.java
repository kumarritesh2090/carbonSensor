package com.example.ritesh.carboncollector.service;

import com.example.ritesh.carboncollector.mongo.model.Sensor;
import com.example.ritesh.carboncollector.mongo.repository.SensorRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j
public class InitService {

    @Autowired
    private SensorRepository sensorRepository;

    public List<UUID> init() {
        List<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            sensors.add(new Sensor(UUID.randomUUID(), 0f, 0));
        }
        sensorRepository.saveAll(sensors);
        return sensors.stream().map(Sensor::getSensorId).collect(Collectors.toList());
    }
}
