package com.enterprise.eams.assetmodule.mapper;

import com.enterprise.eams.assetmodule.dtos.RegisterAssetRequestDTO;
import com.enterprise.eams.assetmodule.dtos.AssetResponseDTO;
import com.enterprise.eams.assetmodule.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(target="assignedTo",ignore = true)
    Asset toEntity(RegisterAssetRequestDTO registerAssetRequestDTO);

    @Mapping(source="assignedTo.id",target = "assignedUserId")
    @Mapping(source="assignedTo.username",target = "assignedUsername")
    @Mapping(source="assignedTo.email",target = "assignedUserEmail")
    @Mapping(source="assignedTo.role",target = "assignedUserRole")
    AssetResponseDTO toAssetResponseDTO(Asset asset);

    List<AssetResponseDTO> toAssetResponseDTOList(List<Asset> assets);
}
