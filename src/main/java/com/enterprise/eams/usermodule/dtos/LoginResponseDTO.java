package com.enterprise.eams.usermodule.dtos;


import com.enterprise.eams.usermodule.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {

    private Long id;
    private String username;
    private String email;
    private UserRole role;
}
