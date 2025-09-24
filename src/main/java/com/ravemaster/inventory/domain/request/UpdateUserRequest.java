package com.ravemaster.inventory.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotNull(message = "Name cannot be blank")
    private String name;
    @NotNull(message = "Email cannot be blank")
    private String email;
    @NotNull(message = "phone number cannot be blank")
    private String phoneNumber;
}
