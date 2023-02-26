package com.serbest.magazine.backend.mapper;

import com.serbest.magazine.backend.dto.post.*;
import com.serbest.magazine.backend.entity.Post;

import org.springframework.stereotype.Component;

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
                .image(post.getPostImage())
                .username(post.getAuthor().getUsername())
                .profileImage(post.getAuthor().getProfileImage())
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
                .active(true)
                .build();
    }

    public Post postCreateEditorRequestDTOToPost(PostCreateEditorRequestDTO postRequestDTO) throws IOException {
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
        return new FirstFivePostsListDTO(post.getPostId(), post.getPostImage(), post.getTitle());
    }

    public MainPagePostsListDTO postToMainPagePostsListDTO(Post post){
        return MainPagePostsListDTO.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .category(post.getCategory().getName())
                .username(post.getAuthor().getUsername())
                .image(post.getPostImage())
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

}
