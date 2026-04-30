package com.enterprise.eams.usermodule.controller;

import com.enterprise.eams.usermodule.dtos.*;
import com.enterprise.eams.usermodule.services.AuthServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServices authServices;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return new ResponseEntity<>(authServices.registerUser(registerRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginOtpResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return new ResponseEntity<>(authServices.loginUser(loginRequestDTO),HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponseDTO>verifyOtp(@Valid @RequestBody VerifyOtpRequestDTO verifyOtpRequestDTO) {
        return  new ResponseEntity<>(authServices.otpVerification(verifyOtpRequestDTO),HttpStatus.OK);
    }
}
