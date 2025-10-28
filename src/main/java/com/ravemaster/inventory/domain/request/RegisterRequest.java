package com.ravemaster.inventory.domain.request;

import com.ravemaster.inventory.domain.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Role cannot be null")
    private String role;

    @NotBlank(message = "Number cannot be blank")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
