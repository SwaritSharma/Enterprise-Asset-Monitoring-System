package com.enterprise.eams.reportmodule.services;

import com.enterprise.eams.alertmodule.entity.Alert;
import com.enterprise.eams.alertmodule.enums.AlertStatus;
import com.enterprise.eams.alertmodule.repository.AlertRepository;
import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.assetmodule.repositories.AssetRepository;
import com.enterprise.eams.downtimemodule.entity.DowntimeLog;
import com.enterprise.eams.downtimemodule.enums.DowntimeStatus;
import com.enterprise.eams.downtimemodule.repository.DowntimeLogRepository;
import com.enterprise.eams.maintenancemodule.entity.MaintenanceLog;
import com.enterprise.eams.maintenancemodule.enums.MaintenanceStatus;
import com.enterprise.eams.maintenancemodule.repository.MaintenanceLogRepository;
import com.enterprise.eams.reportmodule.dtos.AssetHealthDTO;
import com.enterprise.eams.reportmodule.dtos.DashboardSummaryDTO;
import com.enterprise.eams.reportmodule.dtos.DowntimeReportDTO;
import com.enterprise.eams.reportmodule.dtos.MaintenanceReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportServices {

    private final AssetRepository assetRepository;
    private final AlertRepository alertRepository;
    private final DowntimeLogRepository downtimeLogRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;

    public DashboardSummaryDTO getDashboardSummary() {

        long totalAssets = (long) assetRepository.findAll().size();
        long activeAlerts = (long) alertRepository.findAllByStatus(AlertStatus.ACTIVE).size();
        long acknowledgedAlerts = (long) alertRepository.findAllByStatus(AlertStatus.ACKNOWLEDGED).size();
        long resolvedAlerts = (long) alertRepository.findAllByStatus(AlertStatus.RESOLVED).size();
        long activeDowntimes = (long) downtimeLogRepository.findAllByStatus(DowntimeStatus.DOWN).size();
        long assetsUnderMaintenance = (long) maintenanceLogRepository.findAllByStatus(MaintenanceStatus.IN_PROGRESS).size();

        Set<Long> unhealthyAssetIds = new HashSet<>();

        for (Alert alert : alertRepository.findAllByStatus(AlertStatus.ACTIVE)) {
            unhealthyAssetIds.add(alert.getAsset().getId());
        }

        for (DowntimeLog downtimeLog : downtimeLogRepository.findAllByStatus(DowntimeStatus.DOWN)) {
            unhealthyAssetIds.add(downtimeLog.getAsset().getId());
        }

        for (MaintenanceLog maintenanceLog : maintenanceLogRepository.findAllByStatus(MaintenanceStatus.IN_PROGRESS)) {
            unhealthyAssetIds.add(maintenanceLog.getAsset().getId());
        }

        Long healthyAssets = totalAssets - unhealthyAssetIds.size();

        DashboardSummaryDTO dashboardSummaryDTO = new DashboardSummaryDTO();

        dashboardSummaryDTO.setTotalAssets(totalAssets);
        dashboardSummaryDTO.setActiveAlerts(activeAlerts);
        dashboardSummaryDTO.setAcknowledgedAlerts(acknowledgedAlerts);
        dashboardSummaryDTO.setResolvedAlerts(resolvedAlerts);
        dashboardSummaryDTO.setActiveDowntimes(activeDowntimes);
        dashboardSummaryDTO.setAssetsUnderMaintenance(assetsUnderMaintenance);
        dashboardSummaryDTO.setHealthyAssets(healthyAssets);

        return dashboardSummaryDTO;
    }

    public MaintenanceReportDTO getMaintenanceReport() {
        MaintenanceReportDTO maintenanceReportDTO = new MaintenanceReportDTO();
        maintenanceReportDTO.setTotalMaintenanceTasks((long) maintenanceLogRepository.count());
        maintenanceReportDTO.setScheduledTasks((long) maintenanceLogRepository.findAllByStatus(MaintenanceStatus.SCHEDULED).size());
        maintenanceReportDTO.setInProgressTasks((long) maintenanceLogRepository.findAllByStatus(MaintenanceStatus.IN_PROGRESS).size());
        maintenanceReportDTO.setCompletedTasks((long) maintenanceLogRepository.findAllByStatus(MaintenanceStatus.COMPLETED).size());
        maintenanceReportDTO.setOverdueTasks((long) maintenanceLogRepository.findAllByScheduledDateIsBeforeAndStatus(LocalDateTime.now(), MaintenanceStatus.SCHEDULED).size());
        return maintenanceReportDTO;
    }

    public DowntimeReportDTO getDowntimeReport() {

        DowntimeReportDTO dto = new DowntimeReportDTO();

        long totalDowntimes = downtimeLogRepository.count();

        long activeDowntimes =
                downtimeLogRepository.findAllByStatus(DowntimeStatus.DOWN).size();

        long resolvedDowntimes =
                downtimeLogRepository.findAllByStatus(DowntimeStatus.UP).size();

        double totalHours = 0;

        for (DowntimeLog log : downtimeLogRepository.findAll()) {

            if (log.getStartTime() != null && log.getEndTime() != null) {

                long hours = java.time.Duration
                        .between(log.getStartTime(), log.getEndTime())
                        .toHours();

                totalHours += hours;
            }
        }

        dto.setTotalDowntimes(totalDowntimes);
        dto.setActiveDowntimes(activeDowntimes);
        dto.setResolvedDowntimes(resolvedDowntimes);
        dto.setTotalDowntimeHours(totalHours);

        return dto;
    }

    public List<AssetHealthDTO> getAssetHealthReport() {

        List<AssetHealthDTO> response = new ArrayList<>();

        for (Asset asset : assetRepository.findAll()) {

            AssetHealthDTO dto = new AssetHealthDTO();

            dto.setAssetId(asset.getId());
            dto.setAssetName(asset.getName());

            boolean alertActive =
                    !alertRepository
                            .findAllByAssetAndStatus(asset, AlertStatus.ACTIVE)
                            .isEmpty();

            boolean downtimeActive =
                    !downtimeLogRepository
                            .findAllByAssetAndStatus(asset, DowntimeStatus.DOWN)
                            .isEmpty();

            boolean maintenanceActive =
                    !maintenanceLogRepository
                            .findAllByAssetAndStatus(asset, MaintenanceStatus.IN_PROGRESS)
                            .isEmpty();

            dto.setAlertActive(alertActive);
            dto.setDowntimeActive(downtimeActive);
            dto.setUnderMaintenance(maintenanceActive);

            String overallStatus = "HEALTHY";

            if (downtimeActive) {
                overallStatus = "DOWN";
            }
            else if (maintenanceActive) {
                overallStatus = "MAINTENANCE";
            }
            else if (alertActive) {
                overallStatus = "WARNING";
            }

            dto.setOverallStatus(overallStatus);

            response.add(dto);
        }

        return response;
    }


}