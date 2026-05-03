package com.enterprise.eams.assetmodule.dtos;

import com.enterprise.eams.usermodule.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "name",
        "type",
        "location",
        "thresholdTemp",
        "thresholdPressure",
        "assignedUserId",
        "assignedUsername",
        "assignedUserEmail",
        "assignedUserRole"
})
public class AssetResponseDTO {

    private Long id;
    private String name;
    private String type;
    private String location;
    private Double thresholdTemp;
    private Double thresholdPressure;
    private Long assignedUserId;
    private String assignedUsername;
    private String assignedUserEmail;
    private UserRole assignedUserRole;
}
