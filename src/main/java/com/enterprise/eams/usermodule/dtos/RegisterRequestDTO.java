package com.enterprise.eams.usermodule.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {

    @NotBlank(message = "Username must be valid")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email Invalid")
    private String email;

    @NotBlank(message = "Password must be valid")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "Password must contain at least 8 characters, one uppercase, " +
                    "one lowercase, one number and one special character")
    private String password;
}
