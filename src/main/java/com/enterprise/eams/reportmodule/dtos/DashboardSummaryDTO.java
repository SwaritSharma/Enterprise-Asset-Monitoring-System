package com.enterprise.eams.reportmodule.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DashboardSummaryDTO {

    private Long totalAssets;
    private Long activeAlerts;
    private Long acknowledgedAlerts;
    private Long resolvedAlerts;
    private Long activeDowntimes;
    private Long assetsUnderMaintenance;
    private Long healthyAssets;

}
