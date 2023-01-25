package com.serbest.magazine.backend.dto.comment;

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
public class CommentResponseDTO {

    private UUID commentId;
    private String content;
    private String postId;
    private String username;
    private UUID userImageId;
    private String userImageMimType;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
