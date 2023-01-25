package com.serbest.magazine.backend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {
    private UUID id;
    private String title;
    private String subtitle;
    private String content;
    private String category;
    private String username;
    private UUID profileImageId;
    private String profileImageMimType;
    private UUID imageId;
    private String imageMimType;
    private Long comments;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
