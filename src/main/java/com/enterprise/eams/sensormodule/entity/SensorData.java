package com.enterprise.eams.sensormodule.entity;

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
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double pressure;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist()
    public void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
