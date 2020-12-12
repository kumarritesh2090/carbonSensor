package com.example.ritesh.carboncollector.postgres.repository;

import com.example.ritesh.carboncollector.postgres.model.SensorStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SensorStatsRepository extends JpaRepository<SensorStats, Integer> {

    SensorStats findBySensorid(UUID sensorid);
}
