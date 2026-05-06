package com.enterprise.eams.maintenancemodule.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MaintenanceRegisterRequestDTO {

    @NotNull(message = "Asset ID cannot be null")
    private Long assetId;

    @NotNull(message = "Scheduled Date Required")
    @FutureOrPresent(message = "Scheduled Date must be in the present or future")
    private LocalDateTime scheduledDate;

    private String remarks;

}
