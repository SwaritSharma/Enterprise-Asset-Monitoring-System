package com.enterprise.eams.alertmodule.repository;

import com.enterprise.eams.alertmodule.entity.Alert;
import com.enterprise.eams.alertmodule.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {

   Optional<Alert> findByAssetIdAndStatusIn(Long assetId, List<AlertStatus> statuses);

}
