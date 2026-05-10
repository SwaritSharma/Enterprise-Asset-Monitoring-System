package com.enterprise.eams.usermodule.controller;

import com.enterprise.eams.usermodule.dtos.UserResponseDTO;
import com.enterprise.eams.usermodule.dtos.UserRoleUpdateRequestDto;
import com.enterprise.eams.usermodule.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserServices userService;


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateUserRole(@PathVariable Long id, @RequestBody UserRoleUpdateRequestDto userRoleUpdateRequestDto) {
        return new ResponseEntity<>(userService.updateRole(id,userRoleUpdateRequestDto),HttpStatus.OK);
    }
}
