package com.enterprise.eams.downtimemodule.repository;


import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.downtimemodule.entity.DowntimeLog;
import com.enterprise.eams.downtimemodule.enums.DowntimeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DowntimeLogRepository extends JpaRepository<DowntimeLog, Long> {

    List<DowntimeLog> findByAssetIdOrderByStartTimeDesc(Long assetId);

    Optional<DowntimeLog> findByAssetIdAndEndTimeIsNull(Long assetId);

    List<DowntimeLog> findAllByStatus(DowntimeStatus status);
    List<DowntimeLog> findAllByAssetAndStatus(
            Asset asset,
            DowntimeStatus status
    );
    DowntimeLog findByAssetId(Long id);
}
