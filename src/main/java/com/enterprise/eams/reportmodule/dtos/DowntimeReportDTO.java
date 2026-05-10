package com.enterprise.eams.reportmodule.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DowntimeReportDTO {

    private long totalDowntimes;
    private long activeDowntimes;
    private long resolvedDowntimes;
    private double totalDowntimeHours;

}