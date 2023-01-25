package com.serbest.magazine.backend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    @NotBlank(message = "Please provide your content.")
    private String content;

    @NotBlank(message = "Post not found.")
    private String postId;

}
