package com.enterprise.eams.usermodule.mapper;


import com.enterprise.eams.usermodule.dtos.LoginResponseDTO;
import com.enterprise.eams.usermodule.dtos.RegisterRequestDTO;
import com.enterprise.eams.usermodule.dtos.RegisterResponseDTO;
import com.enterprise.eams.usermodule.dtos.UserResponseDTO;
import com.enterprise.eams.usermodule.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequestDTO registerRequestDTO);

    RegisterResponseDTO toRegisterResponseDto(User user);

    LoginResponseDTO toLoginResponseDto(User user);

    UserResponseDTO toUserResponseDto(User user);
    List<UserResponseDTO> toUserResponseDtoList(List<User> users);
}
