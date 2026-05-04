package com.enterprise.eams.alertmodule.repository;

import com.enterprise.eams.alertmodule.entity.Alert;
import com.enterprise.eams.alertmodule.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {

   Optional<Alert> findByAssetIdAndStatus(Long assetId, AlertStatus status);

}
