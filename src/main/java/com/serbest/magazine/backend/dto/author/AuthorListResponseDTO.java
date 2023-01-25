package com.serbest.magazine.backend.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorListResponseDTO {
    private UUID id;
    private String username;
}
