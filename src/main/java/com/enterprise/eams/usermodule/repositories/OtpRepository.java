package com.enterprise.eams.usermodule.repositories;

import com.enterprise.eams.usermodule.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmail(String email);
    void deleteByEmail(String email);
    void deleteByExpiryTimeBefore(LocalDateTime time);
}
