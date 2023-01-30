package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.post.*;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public interface PostService {
    PostCreateResponseDTO createPost(PostRequestDTO requestDTO) throws IOException;
    PostCreateResponseDTO createPostEditor(PostCreateEditorRequestDTO requestDTO) throws IOException;
    List<PostResponseDTO> getAllPosts();
    List<FirstFivePostsListDTO> getFirstFivePosts();
    List<MainPagePostsListDTO> getFourPostsForTop();
    List<MainPagePostsListDTO> getPostsForMainPage();
    List<DeactivatedPostApiResponseDTO> getDeactivatedPost();
    PostResponseDTO findById(String id);
    PostResponseDTO updatePost(String id, PostUpdateRequestDTO requestDTO) throws IOException;
    PostResponseDTO updatePostEditor(String id, PostUpdateEditorRequestDTO requestDTO) throws IOException;
    PostResponseDTO deactivatePost(String id) throws AccessDeniedException;
    PostResponseDTO activatePost(String id);
    List<PostResponseDTO> getRandomThreePost();
    List<PostResponseDTO> getPostsByCategory(String categoryName);
    List<PostResponseDTO> findByUsername(String userId);
    Integer countsByCategoryName(String categoryName);
}
