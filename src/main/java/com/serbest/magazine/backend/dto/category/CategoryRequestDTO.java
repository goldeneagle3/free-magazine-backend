package com.serbest.magazine.backend.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "Fill in the name field!")
    @Length(max = 20, message = "Category's name cannot be more than 20 characters.")
    private String name;
}
