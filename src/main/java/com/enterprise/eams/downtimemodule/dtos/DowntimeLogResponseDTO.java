package com.enterprise.eams.downtimemodule.dtos;

import com.enterprise.eams.downtimemodule.enums.DowntimeReason;
import com.enterprise.eams.downtimemodule.enums.DowntimeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class DowntimeLogResponseDTO {

    private Long id;
    private Long assetId;
    private String assetName;
    private DowntimeReason reason;
    private DowntimeStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;

}
