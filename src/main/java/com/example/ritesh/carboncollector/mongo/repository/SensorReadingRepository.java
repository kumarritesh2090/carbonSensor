package com.example.ritesh.carboncollector.mongo.repository;

import com.example.ritesh.carboncollector.mongo.model.Sensor;
import com.example.ritesh.carboncollector.mongo.model.SensorReading;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SensorReadingRepository extends MongoRepository<SensorReading, UUID> {
    @Aggregation(pipeline = {"{'$match':{time:{$gte: ?0, $lt: ?1}}}", "{$group:{_id: '$sensorId',averageCo2Level: { $avg: '$co2' },maxCo2Level: {$max:'$co2' }}}"})
    AggregationResults<Sensor> getNewSensorStats(String beginDate, String endDate);
}
