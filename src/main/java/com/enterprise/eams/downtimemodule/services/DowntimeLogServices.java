package com.enterprise.eams.downtimemodule.services;

import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.assetmodule.exception.AssetNotFoundException;
import com.enterprise.eams.assetmodule.repositories.AssetRepository;
import com.enterprise.eams.downtimemodule.dtos.DowntimeLogResponseDTO;
import com.enterprise.eams.downtimemodule.entity.DowntimeLog;
import com.enterprise.eams.downtimemodule.enums.DowntimeReason;
import com.enterprise.eams.downtimemodule.enums.DowntimeStatus;
import com.enterprise.eams.downtimemodule.exception.DowntimeLogAlreadyExistsException;
import com.enterprise.eams.downtimemodule.exception.DowntimeLogNotFoundException;
import com.enterprise.eams.downtimemodule.mapper.DowntimeLogMapper;
import com.enterprise.eams.downtimemodule.repository.DowntimeLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DowntimeLogServices {

    private final AssetRepository assetRepository;
    private final DowntimeLogRepository downtimeLogRepository;
    private final DowntimeLogMapper downtimeLogMapper;


    @Transactional
    public void startDowntime(Asset asset, DowntimeReason reason, Long referenceId){

        Optional<DowntimeLog> existingLog = downtimeLogRepository.findByAssetIdAndEndTimeIsNull(asset.getId());

        if(existingLog.isPresent()){
            throw new DowntimeLogAlreadyExistsException("Downtime Log already exists for asset with id: "+asset.getId()+" with reason: "+existingLog.get().getReason());
        }

        DowntimeLog downtimeLog = new DowntimeLog();
        downtimeLog.setAsset(asset);
        downtimeLog.setReason(reason);
        downtimeLog.setReferenceId(referenceId);

        downtimeLogRepository.save(downtimeLog);
    }

    @Transactional
    public void endDowntime(Asset asset){
        DowntimeLog downtimeLog=downtimeLogRepository.findByAssetIdAndEndTimeIsNull(asset.getId()).orElseThrow(()->new DowntimeLogNotFoundException("No active downtime log found for asset with id: "+asset.getId()));
        downtimeLog.setEndTime(LocalDateTime.now());
        downtimeLog.setStatus(DowntimeStatus.UP);
        downtimeLog.setDuration(Duration.between(downtimeLog.getStartTime(), downtimeLog.getEndTime()).toMinutes());
        downtimeLogRepository.save(downtimeLog);
    }

    public List<DowntimeLogResponseDTO> getAllDowntimeLogs() {
        List<DowntimeLog> downtimeLogs = downtimeLogRepository.findAll();
        return downtimeLogMapper.toDowntimeLogResponseDTOs(downtimeLogs);
    }

    public List<DowntimeLogResponseDTO> getDowntimeLogsByAssetId(Long assetId) {
        assetRepository.findById(assetId).orElseThrow(()->new AssetNotFoundException("Asset not found with id: "+assetId));
        List<DowntimeLog> downtimeLogs = downtimeLogRepository.findByAssetIdOrderByStartTimeDesc(assetId);
        return downtimeLogMapper.toDowntimeLogResponseDTOs(downtimeLogs);
    }


    public DowntimeLogResponseDTO getDowntimeLogById(Long id) {
        DowntimeLog downtimeLog=downtimeLogRepository.findById(id).orElseThrow(()-> new DowntimeLogNotFoundException("Downtime log not found with id: "+id));
        return downtimeLogMapper.toDowntimeLogResponseDTO(downtimeLog);
    }

}
