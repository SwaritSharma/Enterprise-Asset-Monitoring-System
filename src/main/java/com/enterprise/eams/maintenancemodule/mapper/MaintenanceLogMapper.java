package com.enterprise.eams.maintenancemodule.mapper;

import com.enterprise.eams.maintenancemodule.dtos.MaintenanceLogResponseDTO;
import com.enterprise.eams.maintenancemodule.dtos.MaintenanceRegisterRequestDTO;
import com.enterprise.eams.maintenancemodule.entity.MaintenanceLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaintenanceLogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "completedDate", ignore = true)
    MaintenanceLog toEntity(MaintenanceRegisterRequestDTO  maintenanceRegisterRequestDTO);

    @Mapping(source = "asset.id",target ="assetId" )
    @Mapping(source = "asset.name",target ="assetName" )
    MaintenanceLogResponseDTO toMaintenanceLogResponseDTO(MaintenanceLog maintenanceLog);

    List<MaintenanceLogResponseDTO> toMaintenanceLogResponseDTOList(List<MaintenanceLog> maintenanceLogs);
}
