package com.enterprise.eams.sensormodule.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SensorDataRequestDTO {

    @NotNull(message = "Asset ID is required")
    @Positive(message = "Asset ID must be a positive number")
    private Long assetId;

    @NotNull(message = "Temperature is required")
    private Double temperature;

    @NotNull(message = "Pressure is required")
    @Positive(message = "Pressure must be positive")
    private Double pressure;
}
