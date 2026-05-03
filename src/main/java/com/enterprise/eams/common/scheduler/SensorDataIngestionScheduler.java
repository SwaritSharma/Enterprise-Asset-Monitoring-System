package com.enterprise.eams.common.scheduler;


import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.assetmodule.repositories.AssetRepository;
import com.enterprise.eams.sensormodule.dtos.SensorDataRequestDTO;
import com.enterprise.eams.sensormodule.services.SensorDataServices;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SensorDataIngestionScheduler {

    private final AssetRepository assetRepository;
    private final SensorDataServices sensorDataService;

    private final Random random = new Random();

    @Scheduled(fixedRate = 10000)
    public void generateSensorData() {

        List<Asset> assets = assetRepository.findAll();

        for (Asset asset : assets) {

            SensorDataRequestDTO dto = new SensorDataRequestDTO();
            dto.setAssetId(asset.getId());

            double tempThreshold = asset.getThresholdTemp();

            double temperature;
            if (random.nextBoolean()) {
                temperature = tempThreshold - (5 + random.nextDouble() * 15);
            } else {
                temperature = tempThreshold + (5 + random.nextDouble() * 20);
            }

            double pressureThreshold = asset.getThresholdPressure();

            double pressure;
            if (random.nextBoolean()) {
                pressure = pressureThreshold - (5 + random.nextDouble() * 15);
            } else {
                pressure = pressureThreshold + (5 + random.nextDouble() * 20);
            }

            dto.setTemperature(temperature);
            dto.setPressure(pressure);

            sensorDataService.saveSensorData(dto);
        }
    }
}