package com.enterprise.eams.usermodule.services;


import com.enterprise.eams.common.services.EmailServices;
import com.enterprise.eams.usermodule.dtos.*;
import com.enterprise.eams.usermodule.entity.Otp;
import com.enterprise.eams.usermodule.entity.User;
import com.enterprise.eams.usermodule.enums.UserRole;
import com.enterprise.eams.usermodule.exception.InvalidOtpOrSessionException;
import com.enterprise.eams.usermodule.exception.InvalidPasswordException;
import com.enterprise.eams.usermodule.exception.UserAlreadyExistsException;
import com.enterprise.eams.usermodule.exception.UserNotFoundException;
import com.enterprise.eams.usermodule.mapper.UserMapper;
import com.enterprise.eams.usermodule.repositories.OtpRepository;
import com.enterprise.eams.usermodule.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailServices emailService;
    private final Map<String,String> tokenStore;
    @Value("${eams.manager.domain}")
    private String domain;

    public RegisterResponseDTO registerUser(@Valid RegisterRequestDTO registerRequestDTO) {
        registerRequestDTO.setEmail(registerRequestDTO.getEmail().trim().toLowerCase());
        if(userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User already exists " + registerRequestDTO.getEmail());
        }
        User user = userMapper.toEntity(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        if(registerRequestDTO.getEmail().endsWith(domain)) {
            user.setRole(UserRole.MANAGER);
        }
        else{
            user.setRole(UserRole.OPERATOR);
        }
        RegisterResponseDTO registered=userMapper.toRegisterResponseDto(userRepository.save(user));

        emailService.sendEmail(registered.getEmail(),
                "Welcome To EAMS",
                "Hello " + registered.getUsername() +
                        "\nYou have been successfully registered on EAMS as "+registered.getRole());
        return registered;
    }

    public LoginOtpResponseDTO loginUser(@Valid LoginRequestDTO loginRequestDTO) {
        loginRequestDTO.setEmail(loginRequestDTO.getEmail().trim().toLowerCase());
        User u=userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()
                ->new UserNotFoundException("User email not found "+loginRequestDTO.getEmail()));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(),u.getPassword())){
            throw new InvalidPasswordException("Invalid password");
        }

        String otp=String.valueOf(100000 + new Random().nextInt(900000));
        String token= UUID.randomUUID().toString();

        tokenStore.put(token,u.getEmail());
        otpRepository.deleteByEmail(u.getEmail());

        Otp verificationOtp=new Otp();
        verificationOtp.setOtp(otp);
        verificationOtp.setEmail(u.getEmail());
        verificationOtp.setExpiryTime(LocalDateTime.now().plusMinutes(2));

        otpRepository.save(verificationOtp);

        emailService.sendEmail(
                u.getEmail(),
                "One Time Login Password",
                "Your One Time Login Password is \n" +
                        otp+"\n One Time Password Is Valid Only For 2 minutes"
        );

        LoginOtpResponseDTO loginOtpResponseDTO=new LoginOtpResponseDTO();
        loginOtpResponseDTO.setEmail(u.getEmail());
        loginOtpResponseDTO.setToken(token);
        return loginOtpResponseDTO;
    }

    @Transactional
    public LoginResponseDTO otpVerification(@Valid VerifyOtpRequestDTO verifyOtpRequestDTO) {
        String loginEmail=tokenStore.get(verifyOtpRequestDTO.getTempToken());

        if(loginEmail==null){
            throw new InvalidOtpOrSessionException("Invalid or Expired Session");
        }

        Otp storedOtp=otpRepository.findByEmail(loginEmail).orElseThrow(()->new InvalidOtpOrSessionException("Invalid or Expired Session"));

        if(storedOtp.getExpiryTime().isBefore(LocalDateTime.now())){
            otpRepository.deleteByEmail(loginEmail);
            tokenStore.remove(verifyOtpRequestDTO.getTempToken());
            throw new InvalidOtpOrSessionException("OTP is expired");
        }
        if(!storedOtp.getOtp().equals(verifyOtpRequestDTO.getOtp())){
            throw new InvalidOtpOrSessionException("Invalid OTP");
        }
        User u=userRepository.findByEmail(loginEmail).orElseThrow(()->new UserNotFoundException("User email not found "+loginEmail));
        otpRepository.deleteByEmail(loginEmail);
        tokenStore.remove(verifyOtpRequestDTO.getTempToken());
        return userMapper.toLoginResponseDto(u);
    }
}