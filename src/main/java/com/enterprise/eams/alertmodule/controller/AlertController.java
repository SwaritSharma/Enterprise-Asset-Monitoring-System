package com.enterprise.eams.alertmodule.controller;

import com.enterprise.eams.alertmodule.dtos.AlertResponseDTO;
import com.enterprise.eams.alertmodule.services.AlertServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertServices alertService;

    @GetMapping
    public ResponseEntity<List<AlertResponseDTO>> getAllAlerts() {
        return new ResponseEntity<>(alertService.getAllAlerts(), HttpStatus.OK);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<AlertResponseDTO> resolveAlert(@PathVariable Long id) {
        return new ResponseEntity<>(alertService.resolveAlert(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<AlertResponseDTO> acknowledgeAlert(@PathVariable Long id) {
        return new ResponseEntity<>(alertService.acknowledgeAlert(id), HttpStatus.OK);
    }


}
