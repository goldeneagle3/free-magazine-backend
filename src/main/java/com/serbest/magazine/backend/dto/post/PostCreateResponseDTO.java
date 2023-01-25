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
public class PostCreateResponseDTO {
    private UUID id;
    private String title;
    private String subtitle;
    private String content;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
