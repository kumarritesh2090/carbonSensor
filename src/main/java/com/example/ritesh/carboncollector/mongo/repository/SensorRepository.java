package com.example.ritesh.carboncollector.mongo.repository;

import com.example.ritesh.carboncollector.mongo.model.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SensorRepository extends MongoRepository<Sensor, UUID> {

}
