package com.enterprise.eams.maintenancemodule.dtos;

import com.enterprise.eams.maintenancemodule.enums.MaintenanceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MaintenanceLogResponseDTO {

    private Long id;
    private Long assetId;
    private String assetName;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledDate;
    private MaintenanceStatus status;
    private String remarks;
    private LocalDateTime completedDate;
    private LocalDateTime updatedAt;

}
