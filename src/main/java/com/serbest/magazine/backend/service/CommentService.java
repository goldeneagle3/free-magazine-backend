package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.comment.CommentRequestDTO;
import com.serbest.magazine.backend.dto.comment.CommentResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Comment;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentResponseDTO createComment(CommentRequestDTO requestDTO);
    List<CommentResponseDTO> getAllComments(String postId);
    Comment findById(String id);
    MessageResponseDTO deleteById(String id) throws AccessDeniedException;
    CommentResponseDTO updateComment(String id, String content) throws AccessDeniedException;

}
