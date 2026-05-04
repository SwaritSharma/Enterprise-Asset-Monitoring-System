package com.enterprise.eams.alertmodule.dtos;

import com.enterprise.eams.alertmodule.enums.AlertStatus;
import com.enterprise.eams.alertmodule.enums.AlertType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AlertResponseDTO {
    private Long id;
    private Long assetId;
    private String assetName;
    private AlertType type;
    private AlertStatus status;
    private String message;
    private LocalDateTime triggeredAt;
    private LocalDateTime resolvedAt;
}
