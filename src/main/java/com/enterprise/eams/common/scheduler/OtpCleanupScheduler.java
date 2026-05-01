package com.enterprise.eams.common.scheduler;

import com.enterprise.eams.usermodule.repositories.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OtpCleanupScheduler {

    private final OtpRepository otpRepository;

    @Scheduled(fixedRate = 30000)
    public void cleanExpiredOtps() {
        otpRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }
}