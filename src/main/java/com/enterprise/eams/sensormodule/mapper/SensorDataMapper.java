package com.enterprise.eams.sensormodule.mapper;

import com.enterprise.eams.sensormodule.dtos.SensorDataRequestDTO;
import com.enterprise.eams.sensormodule.dtos.SensorDataResponseDTO;
import com.enterprise.eams.sensormodule.entity.SensorData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensorDataMapper {

    @Mapping(target ="asset" , ignore = true)
    SensorData toEntity(SensorDataRequestDTO sensorDataRequestDTO);

    @Mapping(source = "asset.id", target = "assetId")
    @Mapping(target = "temperatureDelta", ignore = true)
    @Mapping(target = "temperatureExceeded", ignore = true)
    @Mapping(target = "pressureDelta", ignore = true)
    @Mapping(target = "pressureExceeded", ignore = true)
    SensorDataResponseDTO toSensorDataResponseDTO(SensorData sensorData);

    List<SensorDataResponseDTO> toSensorDataResponseDTOList(List<SensorData> sensorData);
}
