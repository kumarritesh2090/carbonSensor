package com.example.ritesh.carboncollector.postgres.repository;

import com.example.ritesh.carboncollector.postgres.model.Alerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alerts, Integer> {

    List<Alerts> findAlertsBySensorId(UUID sensorId);

    @Modifying
    int deleteAlertsBySensorId(UUID sensorId);

    @Modifying
    @Query(value = "UPDATE Alerts SET endTime=:endTime WHERE sensorId=:sensorId")
    int updateAlertsEndTimeBySensorId(UUID sensorId, Date endTime);
}
