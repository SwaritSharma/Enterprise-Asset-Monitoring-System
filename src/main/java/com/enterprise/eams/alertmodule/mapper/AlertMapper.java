package com.enterprise.eams.alertmodule.mapper;

import com.enterprise.eams.alertmodule.dtos.AlertResponseDTO;
import com.enterprise.eams.alertmodule.entity.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    @Mapping(target = "assetId",source = "asset.id")
    @Mapping(target = "assetName",source = "asset.name")
    AlertResponseDTO toAlertResponseDTO(Alert alert);

    List<AlertResponseDTO> toAlertResponseDTOList(List<Alert> alerts);
}
