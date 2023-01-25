package com.serbest.magazine.backend.dto.user;

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
public class AuthorUpdateRequestDTO {
    private String firstName;
    private String lastName;

    @Size(max = 250, message = "Please provide a bio less than 250 characters.")
    private String description;

    private MultipartFile image;
    private Boolean imageProtect;
}
