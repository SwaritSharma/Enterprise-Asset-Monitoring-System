package com.enterprise.eams.usermodule.dtos;

import com.enterprise.eams.usermodule.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRoleUpdateRequestDto {

    @NotNull(message = "Role Required")
    private UserRole role;
}
