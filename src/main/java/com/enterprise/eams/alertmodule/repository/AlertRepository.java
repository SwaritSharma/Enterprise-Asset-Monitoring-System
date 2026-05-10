package com.enterprise.eams.alertmodule.repository;

import com.enterprise.eams.alertmodule.entity.Alert;
import com.enterprise.eams.alertmodule.enums.AlertStatus;
import com.enterprise.eams.assetmodule.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {

   Optional<Alert> findFirstByAssetIdAndStatusIn(Long assetId, List<AlertStatus> statuses);

   List<Alert> findAllByStatus(AlertStatus status);
   List<Alert> findAllByAssetAndStatus(Asset asset, AlertStatus status);
}
