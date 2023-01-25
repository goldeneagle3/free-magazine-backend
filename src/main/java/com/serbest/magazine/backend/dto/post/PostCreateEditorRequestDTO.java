package com.serbest.magazine.backend.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateEditorRequestDTO {
    @NotBlank(message = "Please provide a title.")
    @Size(max = 50,message = "Title size cannot be more than 50 characters.")
    private String title;

    private String subtitle;

    @NotBlank(message = "Please provide a author.")
    private String author;

    @NotBlank(message = "Please provide your content.")
    // @Size(min = 250,message = "Content size cannot be less than 250 characters.")
    private String content;

    @NotBlank(message = "Please provide a category.")
    private String category;

    private MultipartFile image;
}
