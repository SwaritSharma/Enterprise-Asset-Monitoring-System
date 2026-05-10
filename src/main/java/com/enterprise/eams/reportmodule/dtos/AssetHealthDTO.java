package com.enterprise.eams.reportmodule.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssetHealthDTO {

    private Long assetId;
    private String assetName;
    private boolean alertActive;
    private boolean underMaintenance;
    private boolean downtimeActive;
    private String overallStatus;

}
