package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.like.LikeRequestDTO;
import com.serbest.magazine.backend.dto.like.LikeResponseDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface LikeService {

    LikeResponseDTO like(LikeRequestDTO requestDTO) throws AccessDeniedException;

    List<String> likedUsersByPost(String postId);

    List<String> likedUsersByComment(String commentId);
}
