package com.serbest.magazine.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "Provide a valid username or email , please!")
    private String usernameOrEmail;

    @NotBlank(message = "Provide a valid password , please!")
    private String password;
}
