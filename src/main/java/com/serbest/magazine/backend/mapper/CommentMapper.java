package com.serbest.magazine.backend.mapper;


import com.serbest.magazine.backend.dto.comment.CommentRequestDTO;
import com.serbest.magazine.backend.dto.comment.CommentResponseDTO;
import com.serbest.magazine.backend.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment commentRequestDTOToComment(CommentRequestDTO requestDTO){
        return new Comment(requestDTO.getContent());
    }

    public CommentResponseDTO commentToCommentResponseDTO(Comment comment){
        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getPostId().toString())
                .createDateTime(comment.getCreateDateTime())
                .username(comment.getAuthor().getUsername())
                .updateDateTime(comment.getUpdateDateTime())
                .userImage(comment.getAuthor().getProfileImage())
                .build();
    }
}
