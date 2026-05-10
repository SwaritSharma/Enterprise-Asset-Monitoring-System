package com.enterprise.eams.reportmodule.controller;

import com.enterprise.eams.reportmodule.dtos.AssetHealthDTO;
import com.enterprise.eams.reportmodule.dtos.DashboardSummaryDTO;
import com.enterprise.eams.reportmodule.dtos.DowntimeReportDTO;
import com.enterprise.eams.reportmodule.dtos.MaintenanceReportDTO;
import com.enterprise.eams.reportmodule.services.ReportServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportServices reportService;

    @GetMapping("/dashboard-summary")
    public ResponseEntity<DashboardSummaryDTO>  getDashboardSummary() {
        return new ResponseEntity<>(reportService.getDashboardSummary(), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/maintenance-report")
    public ResponseEntity<MaintenanceReportDTO> getMaintenanceReport() {
        return new ResponseEntity<>(reportService.getMaintenanceReport(), org.springframework.http.HttpStatus.OK);
    }
    @GetMapping("/downtime-report")
    public ResponseEntity<DowntimeReportDTO> getDowntimeReport() {
        return ResponseEntity.ok(reportService.getDowntimeReport());
    }

    @GetMapping("/asset-health")
    public ResponseEntity<List<AssetHealthDTO>> getAssetHealthReport() {
        return ResponseEntity.ok(reportService.getAssetHealthReport());
    }

}
