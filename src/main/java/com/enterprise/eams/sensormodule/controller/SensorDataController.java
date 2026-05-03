package com.enterprise.eams.sensormodule.controller;

import com.enterprise.eams.sensormodule.dtos.SensorDataRequestDTO;
import com.enterprise.eams.sensormodule.dtos.SensorDataResponseDTO;
import com.enterprise.eams.sensormodule.services.SensorDataServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors")
public class SensorDataController {

    private final SensorDataServices sensorDataService;

    @PostMapping("/send-data")
    public ResponseEntity<SensorDataResponseDTO>  sendData(@Valid @RequestBody SensorDataRequestDTO sensorDataRequestDTO) {
        return new ResponseEntity<>(sensorDataService.saveSensorData(sensorDataRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/asset/{id}")
    public ResponseEntity<List<SensorDataResponseDTO>>  getSensorData(@PathVariable Long id){
        return new ResponseEntity<>(sensorDataService.getSensorDataForAsset(id), HttpStatus.OK);
    }

}
