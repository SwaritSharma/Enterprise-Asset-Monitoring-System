package com.enterprise.eams.usermodule.services;


import com.enterprise.eams.usermodule.dtos.LoginRequestDTO;
import com.enterprise.eams.usermodule.dtos.LoginResponseDTO;
import com.enterprise.eams.usermodule.dtos.RegisterRequestDTO;
import com.enterprise.eams.usermodule.dtos.RegisterResponseDTO;
import com.enterprise.eams.usermodule.entity.User;
import com.enterprise.eams.usermodule.enums.UserRole;
import com.enterprise.eams.usermodule.exception.InvalidPasswordException;
import com.enterprise.eams.usermodule.exception.UserNotFoundException;
import com.enterprise.eams.usermodule.mapper.UserMapper;
import com.enterprise.eams.usermodule.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final String domain="@eams.com";

    public RegisterResponseDTO registerUser(@Valid RegisterRequestDTO registerRequestDTO) {
        User user = userMapper.toEntity(registerRequestDTO);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        if(registerRequestDTO.getEmail().endsWith(domain)) {
            user.setRole(UserRole.MANAGER);
        }
        else{
            user.setRole(UserRole.OPERATOR);
        }
        return userMapper.toRegisterResponseDto(userRepository.save(user));
    }

    public LoginResponseDTO loginUser(@Valid LoginRequestDTO loginRequestDTO) {
        User u=userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()->new UserNotFoundException("User email not found "+loginRequestDTO.getEmail()));
        if(!passwordEncoder.matches(loginRequestDTO.getPassword(),u.getPassword())){
            throw new InvalidPasswordException("Invalid password");
        }
        return userMapper.toLoginResponseDto(u);
    }
}