package com.enterprise.eams.maintenancemodule.repository;

import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.maintenancemodule.entity.MaintenanceLog;
import com.enterprise.eams.maintenancemodule.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {

    List<MaintenanceLog> findByAssetIdOrderByCreatedAtDesc(Long id);

    List<MaintenanceLog> findAllByStatus(MaintenanceStatus maintenanceStatus);

    List<MaintenanceLog> findAllByScheduledDateIsBeforeAndStatus(LocalDateTime localDateTime, MaintenanceStatus maintenanceStatus);

    List<MaintenanceLog> findAllByAssetAndStatus(
            Asset asset,
            MaintenanceStatus status
    );
}
