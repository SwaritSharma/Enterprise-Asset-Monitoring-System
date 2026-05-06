package com.enterprise.eams.maintenancemodule.entity;

import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.maintenancemodule.enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    private LocalDateTime completedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceStatus status;

    private String remarks;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void created() {
        this.createdAt = LocalDateTime.now();
        if(this.status==null) {
            this.status = MaintenanceStatus.SCHEDULED;
        }
    }

    @PreUpdate
    public void updated() {
        this.updatedAt = LocalDateTime.now();
    }
}
