package com.enterprise.eams.alertmodule.services;

import com.enterprise.eams.alertmodule.dtos.AlertResponseDTO;
import com.enterprise.eams.alertmodule.entity.Alert;
import com.enterprise.eams.alertmodule.enums.AlertStatus;
import com.enterprise.eams.alertmodule.enums.AlertType;
import com.enterprise.eams.alertmodule.exception.AlertAlreadyAcknowledgedException;
import com.enterprise.eams.alertmodule.exception.AlertAlreadyResolvedException;
import com.enterprise.eams.alertmodule.exception.AlertNotFoundException;
import com.enterprise.eams.alertmodule.mapper.AlertMapper;
import com.enterprise.eams.alertmodule.repository.AlertRepository;
import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.common.services.EmailServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServices {

    private final EmailServices emailService;
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    @Transactional
    public void processAlert(Asset asset, double temperature, double pressure) {

        double thresholdTemp = asset.getThresholdTemp();
        double thresholdPressure = asset.getThresholdPressure();

        List<AlertStatus> statuses = new ArrayList<>();
        statuses.add(AlertStatus.ACTIVE);
        statuses.add(AlertStatus.ACKNOWLEDGED);

        Alert alert = alertRepository
                .findFirstByAssetIdAndStatusIn(asset.getId(), statuses)
                .orElse(null);

        boolean tempExceeded = temperature > thresholdTemp;
        boolean pressureExceeded = pressure > thresholdPressure;

        AlertType newAlertType = null;

        if (tempExceeded && pressureExceeded) {
            newAlertType = AlertType.MULTIPLE_PARAMETERS_HIGH;
        } else if (tempExceeded) {
            newAlertType = AlertType.TEMP_HIGH;
        } else if (pressureExceeded) {
            newAlertType = AlertType.PRESSURE_HIGH;
        }

        if (alert == null) {
            if (newAlertType != null) {
                Alert newAlert = new Alert();
                newAlert.setAsset(asset);
                newAlert.setType(newAlertType);
                newAlert.setStatus(AlertStatus.ACTIVE);
                newAlert.setMessage(generateMessage(newAlertType, asset.getName(), temperature, pressure, thresholdTemp, thresholdPressure));
                alertRepository.save(newAlert);
                sendAlertMail(newAlert);
            }
            return;
        }

        if (newAlertType == null) {
            alert.setStatus(AlertStatus.RESOLVED);
            alert.setResolvedAt(LocalDateTime.now());
            alert.setMessage(
                    "Alert for asset " + asset.getName() +
                            " has been resolved. Sensor values are back within acceptable limits."
            );

            alertRepository.save(alert);
            sendAlertMail(alert);
            return;
        }

        if (alert.getType() == newAlertType) {
            return;
        }

        alert.setType(newAlertType);
        alert.setMessage(generateMessage(newAlertType, asset.getName(), temperature, pressure, thresholdTemp, thresholdPressure));

        Alert updatedAlert=alertRepository.save(alert);
        sendAlertMail(updatedAlert);

    }
    private void sendAlertMail(Alert alert) {

        if (alert.getAsset().getAssignedTo() == null) {
            return;
        }

        String assetName = alert.getAsset().getName();
        String userName = alert.getAsset().getAssignedTo().getUsername();

        String subject = "[EAMS] " + alert.getStatus() + " ALERT - " + assetName;

        String intro = switch (alert.getStatus()) {
            case ACTIVE -> "An alert has been triggered for the following asset:\n\n";
            case ACKNOWLEDGED -> "An alert has been acknowledged for the following asset:\n\n";
            case RESOLVED -> "An alert has been resolved for the following asset:\n\n";
        };

        String message =
                "Dear " + userName + ",\n\n" +

                        intro +

                        "Asset Name : " + assetName + "\n" +
                        "Alert Type : " + alert.getType() + "\n" +
                        "Status     : " + alert.getStatus() + "\n" +
                        "Triggered  : " + alert.getTriggeredAt() + "\n" +

                        (alert.getResolvedAt() != null
                                ? "Resolved   : " + alert.getResolvedAt() + "\n"
                                : "") +

                        "\nDetails:\n" +
                        alert.getMessage() + "\n\n" +

                        "Please take necessary action if required.\n\n" +

                        "Regards,\n" +
                        "Enterprise Asset Monitoring System (EAMS)\n";


        emailService.sendEmail(
                alert.getAsset().getAssignedTo().getEmail(),
                subject,
                message
        );
    }
    private String generateMessage(AlertType type, String assetName, double temperature, double pressure, double thresholdTemp, double thresholdPressure) {

        double tempDelta = temperature - thresholdTemp;
        double pressureDelta = pressure - thresholdPressure;

        return switch (type)
        {
            case TEMP_HIGH -> "Asset " + assetName +
                    " temperature exceeded threshold by " +
                    String.format("%.2f", tempDelta);
            case PRESSURE_HIGH -> "Asset " + assetName +
                    " pressure exceeded threshold by " +
                    String.format("%.2f", pressureDelta);
            case MULTIPLE_PARAMETERS_HIGH -> "Asset " + assetName +
                    " temperature exceeded by " +
                    String.format("%.2f", tempDelta) +
                    " and pressure exceeded by " +
                    String.format("%.2f", pressureDelta);
            default -> "Asset " + assetName + " is operating normally";
        };
    }

    public List<AlertResponseDTO> getAllAlerts() {
        List<Alert> alerts = alertRepository.findAll();
        return alertMapper.toAlertResponseDTOList(alerts);
    }

    @Transactional
    public AlertResponseDTO resolveAlert(Long id) {
        Alert alert=alertRepository.findById(id).orElseThrow(()->new AlertNotFoundException("Alert not found with ID: "+id));
        if(alert.getStatus()==AlertStatus.RESOLVED){
            throw new AlertAlreadyResolvedException("Alert with ID: "+id+" is already resolved.");
        }
        alert.setStatus(AlertStatus.RESOLVED);
        alert.setResolvedAt(LocalDateTime.now());

        alert.setMessage(
                "Alert for asset " + alert.getAsset().getName() +
                        " has been manually resolved."
        );

        return alertMapper.toAlertResponseDTO(alertRepository.save(alert));
    }

    @Transactional
    public AlertResponseDTO acknowledgeAlert(Long id) {
        Alert alert=alertRepository.findById(id).orElseThrow(()->new AlertNotFoundException("Alert not found with ID: "+id));
        if(alert.getStatus()==AlertStatus.RESOLVED){
            throw new AlertAlreadyResolvedException("Alert with ID: "+id+" is already resolved and cannot acknowledge");
        }
        if(alert.getStatus()==AlertStatus.ACKNOWLEDGED){
            throw new AlertAlreadyAcknowledgedException("Alert with ID: "+id+" is already acknowledged.");
        }

        alert.setStatus(AlertStatus.ACKNOWLEDGED);
        alert.setMessage(
                "Alert for asset " + alert.getAsset().getName() +
                        " has been acknowledged by the user."
        );
        Alert updatedAlert=alertRepository.save(alert);
        sendAlertMail(updatedAlert);
        return alertMapper.toAlertResponseDTO(updatedAlert);

    }
}
