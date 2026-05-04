package com.enterprise.eams.assetmodule.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterAssetRequestDTO {
    @NotBlank(message = "Asset name is required")
    private String name;

    @NotBlank(message = "Asset type is required")
    private String type;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Temperature threshold is required")
    @Positive(message = "Temperature must be positive")
    private Double thresholdTemp;

    @NotNull(message = "Pressure threshold is required")
    @Positive(message = "Pressure must be positive")
    private Double thresholdPressure;

    @Positive(message = "User ID must be valid")
    private Long assignedUserId;
}
