package com.serbest.magazine.backend.dto.author;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorCardResponseDTO {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String description;
    private String image;
    private String imageName;
}
