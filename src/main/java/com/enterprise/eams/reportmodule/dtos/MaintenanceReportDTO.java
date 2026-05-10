package com.enterprise.eams.reportmodule.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MaintenanceReportDTO
{
    private long totalMaintenanceTasks;
    private long scheduledTasks;
    private long inProgressTasks;
    private long completedTasks;
    private long overdueTasks;
}