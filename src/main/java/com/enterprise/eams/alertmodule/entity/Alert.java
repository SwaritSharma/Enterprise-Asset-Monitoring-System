package com.enterprise.eams.alertmodule.entity;

import com.enterprise.eams.alertmodule.enums.AlertStatus;
import com.enterprise.eams.alertmodule.enums.AlertType;
import com.enterprise.eams.assetmodule.entity.Asset;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id" , nullable = false)
    private Asset asset;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    private AlertStatus status;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    private LocalDateTime resolvedAt;

    @PrePersist
    public void triggerTime() {
        this.triggeredAt = LocalDateTime.now();
    }
}
