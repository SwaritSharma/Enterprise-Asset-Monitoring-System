package com.enterprise.eams.sensormodule.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class SensorDataResponseDTO {

    private Long id;
    private Long assetId;
    private Double temperature;
    private Double temperatureDelta;
    private Boolean temperatureExceeded;
    private Double pressure;
    private Double pressureDelta;
    private Boolean pressureExceeded;
    private LocalDateTime timestamp;

}
