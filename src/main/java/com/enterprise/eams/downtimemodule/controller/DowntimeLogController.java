package com.enterprise.eams.downtimemodule.controller;

import com.enterprise.eams.downtimemodule.dtos.DowntimeLogResponseDTO;
import com.enterprise.eams.downtimemodule.services.DowntimeLogServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/downtime")
public class DowntimeLogController {

    private final DowntimeLogServices downtimeLogService;

    @GetMapping
    public ResponseEntity<List<DowntimeLogResponseDTO>> getAllDowntimeLogs() {
        return new ResponseEntity<>(downtimeLogService.getAllDowntimeLogs(), HttpStatus.OK);
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<DowntimeLogResponseDTO>>  getDowntimeLogsByAssetId(@PathVariable Long assetId){
        return new ResponseEntity<>(downtimeLogService.getDowntimeLogsByAssetId(assetId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DowntimeLogResponseDTO> getDowntimeLogById(@PathVariable Long id){
        return new ResponseEntity<>(downtimeLogService.getDowntimeLogById(id), HttpStatus.OK);
    }
}
