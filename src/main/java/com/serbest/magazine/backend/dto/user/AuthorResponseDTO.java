package com.serbest.magazine.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String description;
    private UUID imageId;
    private String imageExist;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
