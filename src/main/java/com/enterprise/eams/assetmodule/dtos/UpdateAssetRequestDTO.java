package com.enterprise.eams.assetmodule.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateAssetRequestDTO {

    private String name;
    private String type;
    private String location;
    
    @Positive(message = "Temperature must be positive")
    private Double thresholdTemp;

    @Positive(message = "Temperature must be positive")
    private Double thresholdPressure;

    @Positive(message = "User Id must be valid")
    private Long assignedUserId;
}
