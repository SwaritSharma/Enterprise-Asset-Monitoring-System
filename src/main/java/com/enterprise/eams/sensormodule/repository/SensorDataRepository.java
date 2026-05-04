package com.enterprise.eams.sensormodule.repository;

import com.enterprise.eams.sensormodule.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findByAssetIdOrderByTimestampDesc(Long assetId);
}
