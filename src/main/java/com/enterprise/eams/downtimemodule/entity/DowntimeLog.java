package com.enterprise.eams.downtimemodule.entity;

import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.downtimemodule.enums.DowntimeReason;
import com.enterprise.eams.downtimemodule.enums.DowntimeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DowntimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DowntimeReason reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DowntimeStatus  status;

    private Long referenceId;


    @PrePersist
    public void onCreate() {
        this.startTime=LocalDateTime.now();
        this.status = DowntimeStatus.DOWN;
    }




}
