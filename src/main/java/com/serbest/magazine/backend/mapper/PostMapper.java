package com.serbest.magazine.backend.mapper;

import com.serbest.magazine.backend.dto.post.*;
import com.serbest.magazine.backend.entity.ImageModel;
import com.serbest.magazine.backend.entity.Post;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@Component
public class PostMapper {

    public PostResponseDTO postToPostResponseDTO(Post post){
        return PostResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .content(post.getContent())
                .category(post.getCategory().getName())
                .imageId(post.getPostImage().getId())
                .imageMimType(post.getPostImage().getType())
                .username(post.getAuthor().getUsername())
                .profileImageId(post.getAuthor().getProfileImage().getId())
                .profileImageMimType(post.getAuthor().getProfileImage().getName())
                .comments(post.getComments().stream().count())
                .createDateTime(post.getCreateDateTime())
                .updateDateTime(post.getUpdateDateTime())
                .build();
    }

    public Post postRequestDTOToPost(PostRequestDTO postRequestDTO) throws IOException {
        return Post.Builder
                .newBuilder()
                .title(postRequestDTO.getTitle())
                .subtitle(postRequestDTO.getSubtitle())
                .content(postRequestDTO.getContent())
                .postImage(uploadImage(postRequestDTO.getImage()))
                .active(true)
                .build();
    }

    public Post postCreateEditorRequestDTOToPost(PostCreateEditorRequestDTO postRequestDTO) throws IOException {
        return Post.Builder
                .newBuilder()
                .title(postRequestDTO.getTitle())
                .subtitle(postRequestDTO.getSubtitle())
                .content(postRequestDTO.getContent())
                .postImage(uploadImage(postRequestDTO.getImage()))
                .active(true)
                .build();
    }

    public Post postResponseDTOToPostTwo(PostRequestDTO postRequestDTO){
        return Post.Builder
                .newBuilder()
                .title(postRequestDTO.getTitle())
                .subtitle(postRequestDTO.getSubtitle())
                .content(postRequestDTO.getContent())
                .active(true)
                .build();
    }

    public PostCreateResponseDTO postToPostCreateResponseDTO(Post post){
        return PostCreateResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .content(post.getContent())
                .createDateTime(post.getCreateDateTime())
                .updateDateTime(post.getUpdateDateTime())
                .build();
    }

    public FirstFivePostsListDTO postToFirstFivePostsListDTO(Post post) {
        return new FirstFivePostsListDTO(post.getPostId(), post.getPostImage().getId(), post.getTitle());
    }

    public FourPostsForTopResponseDTO postToFourPostsForTopResponseDTO(Post post) {
        return FourPostsForTopResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .username(post.getAuthor().getUsername())
                .category(post.getCategory().getName())
                .imageId(post.getPostImage().getId())
                .build();
    }

    public MainPagePostsListDTO postToMainPagePostsListDTO(Post post){
        return MainPagePostsListDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .category(post.getCategory().getName())
                .username(post.getAuthor().getUsername())
                .imageId(post.getPostImage().getId())
                .imageMimType(post.getPostImage().getName())
                .comments(post.getComments().stream().count())
                .createDateTime(post.getCreateDateTime())
                .build();
    }

    public DeactivatedPostApiResponseDTO postToDeactivatedPostApiResponseDTO(Post post){
        return DeactivatedPostApiResponseDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .category(post.getCategory().getName())
                .username(post.getAuthor().getUsername())
                .build();
    }

    private ImageModel uploadImage(MultipartFile file) throws IOException {
        if (file == null) {
            return new ImageModel();
        }
        return new ImageModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
    }

}
