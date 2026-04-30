package com.enterprise.eams.usermodule.entity;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String otp;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private LocalDateTime expiryTime;

}
