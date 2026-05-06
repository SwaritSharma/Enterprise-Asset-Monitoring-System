package com.enterprise.eams.downtimemodule.repository;


import com.enterprise.eams.downtimemodule.entity.DowntimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DowntimeLogRepository extends JpaRepository<DowntimeLog, Long> {

    List<DowntimeLog> findByAssetIdOrderByStartTimeDesc(Long assetId);

    Optional<DowntimeLog> findByAssetIdAndEndTimeIsNull(Long assetId);

    DowntimeLog findByAssetId(Long id);
}
