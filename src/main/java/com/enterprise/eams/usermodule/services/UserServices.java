package com.enterprise.eams.usermodule.services;

import com.enterprise.eams.usermodule.dtos.UserResponseDTO;
import com.enterprise.eams.usermodule.dtos.UserRoleUpdateRequestDto;
import com.enterprise.eams.usermodule.entity.User;
import com.enterprise.eams.usermodule.exception.UserNotFoundException;
import com.enterprise.eams.usermodule.exception.UserRoleSameException;
import com.enterprise.eams.usermodule.mapper.UserMapper;
import com.enterprise.eams.usermodule.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserResponseDtoList(users);
    }

    public UserResponseDTO updateRole(Long id, UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        User targetUser=userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User not found "+id));
        if(userRoleUpdateRequestDto.getRole()==targetUser.getRole()){
            throw new UserRoleSameException("User already has the same role " + userRoleUpdateRequestDto.getRole());
        }
        targetUser.setRole(userRoleUpdateRequestDto.getRole());
        return userMapper.toUserResponseDto(userRepository.save(targetUser));
    }
}
