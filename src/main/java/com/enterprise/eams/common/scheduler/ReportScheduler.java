package com.enterprise.eams.common.scheduler;

import com.enterprise.eams.reportmodule.dtos.DashboardSummaryDTO;
import com.enterprise.eams.reportmodule.dtos.MaintenanceReportDTO;
import com.enterprise.eams.reportmodule.services.ReportServices;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final ReportServices reportService;

    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailyReport() {

        DashboardSummaryDTO dashboard =
                reportService.getDashboardSummary();

        System.out.println("Daily Report Generated");
        System.out.println(dashboard);
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void generateWeeklyReport() {

        MaintenanceReportDTO maintenance =
                reportService.getMaintenanceReport();

        System.out.println("Weekly Report Generated");
        System.out.println(maintenance);
    }

}