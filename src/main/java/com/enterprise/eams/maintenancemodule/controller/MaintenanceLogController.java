package com.enterprise.eams.maintenancemodule.controller;

import com.enterprise.eams.maintenancemodule.dtos.MaintenanceLogResponseDTO;
import com.enterprise.eams.maintenancemodule.dtos.MaintenanceRegisterRequestDTO;
import com.enterprise.eams.maintenancemodule.services.MaintenanceLogServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/maintenance")
public class MaintenanceLogController {

    private final MaintenanceLogServices maintenanceLogService;

    @PostMapping
    public ResponseEntity<MaintenanceLogResponseDTO> createLog(@Valid @RequestBody MaintenanceRegisterRequestDTO  maintenanceRegisterRequestDTO) {
        return new ResponseEntity<>(maintenanceLogService.createLog(maintenanceRegisterRequestDTO), org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<MaintenanceLogResponseDTO> startMaintenance(@PathVariable Long id) {
        return new ResponseEntity<>(maintenanceLogService.startMaintenance(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<MaintenanceLogResponseDTO> completeMaintenance(@PathVariable Long id) {
        return new ResponseEntity<>(maintenanceLogService.completeMaintenance(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLogResponseDTO> getMaintenanceLogById(@PathVariable Long id) {
        return new ResponseEntity<>(maintenanceLogService.getMaintenanceLogById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceLogResponseDTO>> getAllMaintenanceLogs() {
        return new ResponseEntity<>(maintenanceLogService.getAllMaintenanceLogs(), HttpStatus.OK);
    }

    @GetMapping("/asset/{id}")
    public ResponseEntity<List<MaintenanceLogResponseDTO>> getMaintenanceLogsByAssetId(@PathVariable Long id) {
        return new ResponseEntity<>(maintenanceLogService.getMaintenanceLogsByAssetId(id), HttpStatus.OK);
    }

}
