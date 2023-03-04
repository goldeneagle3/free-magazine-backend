package com.serbest.magazine.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDTO {

    @NotBlank(message = "Fill in the name field!")
    @Length(max = 20,message = "Role name cannot be more than 20 characters.")
    private String name;
}
