package com.serbest.magazine.backend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FourPostsForTopResponseDTO {
    private UUID id;
    private String title;
    private String username;
    private String category;
    private String image;
}
