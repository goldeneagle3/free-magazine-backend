package com.serbest.magazine.backend.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Fill in the username field!")
    @Size(max = 25,message = "Username cannot be more than 25 characters.")
    private String username;

    @NotBlank(message = "Fill in the username field!")
    @Email(message = "Please provide a valid email.")
    private String email;

    private String firstName;
    private String lastName;

    @NotBlank(message = "Fill in the username field!")
    @Size(min = 8,message = "Password cannot be less than 8 characters.")
    private String password;

}
