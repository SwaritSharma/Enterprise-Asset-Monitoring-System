package com.enterprise.eams.usermodule.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginOtpResponseDTO {

    private String email;
    private String token;
}
