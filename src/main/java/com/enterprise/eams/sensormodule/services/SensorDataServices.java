package com.enterprise.eams.sensormodule.services;

import com.enterprise.eams.alertmodule.services.AlertServices;
import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.assetmodule.exception.AssetNotFoundException;
import com.enterprise.eams.assetmodule.repositories.AssetRepository;
import com.enterprise.eams.sensormodule.dtos.SensorDataRequestDTO;
import com.enterprise.eams.sensormodule.dtos.SensorDataResponseDTO;
import com.enterprise.eams.sensormodule.entity.SensorData;
import com.enterprise.eams.sensormodule.mapper.SensorDataMapper;
import com.enterprise.eams.sensormodule.repository.SensorDataRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorDataServices {

    private final AssetRepository assetRepository;
    private final SensorDataRepository sensorDataRepository;
    private final SensorDataMapper sensorDataMapper;
    private final AlertServices alertService;

    @Transactional
    public SensorDataResponseDTO saveSensorData(@Valid SensorDataRequestDTO sensorDataRequestDTO) {
        Asset asset = assetRepository.findById(sensorDataRequestDTO.getAssetId())
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + sensorDataRequestDTO.getAssetId()));

        SensorData sensorData = sensorDataMapper.toEntity(sensorDataRequestDTO);
        sensorData.setAsset(asset);

        SensorData savedSensorData = sensorDataRepository.save(sensorData);

        SensorDataResponseDTO sensorDataResponseDTO= sensorDataMapper.toSensorDataResponseDTO(savedSensorData);

        alertService.processAlert(savedSensorData.getAsset(), savedSensorData.getTemperature(), savedSensorData.getPressure());

        double temperatureDelta=savedSensorData.getTemperature()-asset.getThresholdTemp();
        sensorDataResponseDTO.setTemperatureDelta(temperatureDelta);
        sensorDataResponseDTO.setTemperatureExceeded(temperatureDelta>0);

        double pressureDelta=savedSensorData.getPressure()-asset.getThresholdPressure();
        sensorDataResponseDTO.setPressureDelta(pressureDelta);
        sensorDataResponseDTO.setPressureExceeded(pressureDelta>0);


        return sensorDataResponseDTO;
    }

    public List<SensorDataResponseDTO> getSensorDataForAsset(Long id) {

        Asset asset=assetRepository.findById(id).orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + id));
        List<SensorData> list=sensorDataRepository.findByAssetIdOrderByTimestampDesc(id);
        List<SensorDataResponseDTO> responseList = new ArrayList<>();

        for (SensorData data : list) {
            SensorDataResponseDTO response = sensorDataMapper.toSensorDataResponseDTO(data);


            double temperatureDelta = data.getTemperature() - asset.getThresholdTemp();
            response.setTemperatureDelta(temperatureDelta);
            response.setTemperatureExceeded(temperatureDelta > 0);

            double pressureDelta = data.getPressure() - asset.getThresholdPressure();
            response.setPressureDelta(pressureDelta);
            response.setPressureExceeded(pressureDelta > 0);

            responseList.add(response);
        }

        return responseList;
    }
}
