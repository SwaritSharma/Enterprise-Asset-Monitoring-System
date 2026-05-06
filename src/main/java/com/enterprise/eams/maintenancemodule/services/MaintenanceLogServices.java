package com.enterprise.eams.maintenancemodule.services;

import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.assetmodule.exception.AssetNotFoundException;
import com.enterprise.eams.assetmodule.repositories.AssetRepository;
import com.enterprise.eams.maintenancemodule.dtos.MaintenanceLogResponseDTO;
import com.enterprise.eams.maintenancemodule.dtos.MaintenanceRegisterRequestDTO;
import com.enterprise.eams.maintenancemodule.entity.MaintenanceLog;
import com.enterprise.eams.maintenancemodule.enums.MaintenanceStatus;
import com.enterprise.eams.maintenancemodule.exception.MaintenanceLogNotFoundException;
import com.enterprise.eams.maintenancemodule.exception.MaintenanceLogStatusException;
import com.enterprise.eams.maintenancemodule.mapper.MaintenanceLogMapper;
import com.enterprise.eams.maintenancemodule.repository.MaintenanceLogRepository;
import com.enterprise.eams.downtimemodule.enums.DowntimeReason;
import com.enterprise.eams.downtimemodule.services.DowntimeLogServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceLogServices {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final MaintenanceLogMapper maintenanceLogMapper;
    private final AssetRepository assetRepository;
    private final DowntimeLogServices downtimeLogService;

    @Transactional
    public MaintenanceLogResponseDTO createLog(MaintenanceRegisterRequestDTO maintenanceRegisterRequestDTO) {

        Asset asset=assetRepository.findById(maintenanceRegisterRequestDTO.getAssetId())
                .orElseThrow(()-> new AssetNotFoundException(
                        "Log cannot be created Asset does not Exist with id :"
                                +maintenanceRegisterRequestDTO.getAssetId()));

        MaintenanceLog maintenanceLog = maintenanceLogMapper.toEntity(maintenanceRegisterRequestDTO);
        maintenanceLog.setAsset(asset);
        return maintenanceLogMapper.toMaintenanceLogResponseDTO(maintenanceLogRepository.save(maintenanceLog));
    }

    @Transactional
    public MaintenanceLogResponseDTO startMaintenance(Long id) {
        MaintenanceLog maintenanceLog=maintenanceLogRepository.findById(id)
                .orElseThrow(()-> new MaintenanceLogNotFoundException("Maintenance Log not found with id :"+id));

        if(maintenanceLog.getStatus()!=MaintenanceStatus.SCHEDULED) {
            throw new MaintenanceLogStatusException("Maintenance Log with id :"+id+" cannot be started as it already has status : "+maintenanceLog.getStatus());
        }

        maintenanceLog.setStatus(MaintenanceStatus.IN_PROGRESS);
        maintenanceLogRepository.save(maintenanceLog);
        downtimeLogService.startDowntime(maintenanceLog.getAsset(), DowntimeReason.MAINTENANCE, maintenanceLog.getId());
        return maintenanceLogMapper.toMaintenanceLogResponseDTO(maintenanceLog);
    }

    @Transactional
    public MaintenanceLogResponseDTO completeMaintenance(Long id) {
        MaintenanceLog maintenanceLog=maintenanceLogRepository.findById(id)
                .orElseThrow(()-> new MaintenanceLogNotFoundException("Maintenance Log not found with id :"+id));

        if(maintenanceLog.getStatus()!=MaintenanceStatus.IN_PROGRESS) {
            String exceptionMessage="";
            if(maintenanceLog.getStatus().equals(MaintenanceStatus.SCHEDULED)){
                exceptionMessage="Maintenance Log with id :"+id+" cannot be completed as it is not marked as IN_PROGRESS. Please start the maintenance first.";
            }else if(maintenanceLog.getStatus().equals(MaintenanceStatus.COMPLETED)){
                exceptionMessage="Maintenance Log with id :"+id+" cannot be completed as it is already marked as COMPLETED.";
            }
            throw new MaintenanceLogStatusException(exceptionMessage);
        }

        maintenanceLog.setStatus(MaintenanceStatus.COMPLETED);
        maintenanceLog.setCompletedDate(LocalDateTime.now());

        MaintenanceLog updatedLog=maintenanceLogRepository.save(maintenanceLog);

        downtimeLogService.endDowntime(maintenanceLog.getAsset());

        return maintenanceLogMapper.toMaintenanceLogResponseDTO(updatedLog);
    }

    public MaintenanceLogResponseDTO getMaintenanceLogById(Long id) {
        MaintenanceLog maintenanceLog=maintenanceLogRepository.findById(id).orElseThrow(()-> new MaintenanceLogNotFoundException("Maintenance Log not found with id :"+id));
        return maintenanceLogMapper.toMaintenanceLogResponseDTO(maintenanceLog);
    }

    public List<MaintenanceLogResponseDTO> getAllMaintenanceLogs() {

        List<MaintenanceLog>  maintenanceLogs=maintenanceLogRepository.findAll();
        return maintenanceLogMapper.toMaintenanceLogResponseDTOList(maintenanceLogs);
    }

    public List<MaintenanceLogResponseDTO> getMaintenanceLogsByAssetId(Long id) {
        assetRepository.findById(id).orElseThrow(()-> new AssetNotFoundException("Asset not found with id :"+id));
        List<MaintenanceLog>  maintenanceLogs=maintenanceLogRepository.findByAssetIdOrderByCreatedAtDesc(id);
        return maintenanceLogMapper.toMaintenanceLogResponseDTOList(maintenanceLogs);
    }
}
