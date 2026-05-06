package com.enterprise.eams.downtimemodule.mapper;

import com.enterprise.eams.downtimemodule.dtos.DowntimeLogResponseDTO;
import com.enterprise.eams.downtimemodule.entity.DowntimeLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DowntimeLogMapper {

    @Mapping(source = "asset.id",target = "assetId")
    @Mapping(source = "asset.name",target = "assetName")
    DowntimeLogResponseDTO toDowntimeLogResponseDTO(DowntimeLog downtimeLog);

    List<DowntimeLogResponseDTO> toDowntimeLogResponseDTOs(List<DowntimeLog> downtimeLogs);
}
