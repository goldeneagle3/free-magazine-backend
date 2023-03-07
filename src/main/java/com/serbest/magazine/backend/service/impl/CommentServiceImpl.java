package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.comment.CommentRequestDTO;
import com.serbest.magazine.backend.dto.comment.CommentResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Comment;
import com.serbest.magazine.backend.entity.Post;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.CommentMapper;
import com.serbest.magazine.backend.repository.AuthorRepository;
import com.serbest.magazine.backend.repository.CommentRepository;
import com.serbest.magazine.backend.repository.PostRepository;
import com.serbest.magazine.backend.service.CommentService;

import com.serbest.magazine.backend.security.CheckAuthorization;
import io.jsonwebtoken.lang.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CheckAuthorization checkAuthorization;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorRepository userRepository;


    public CommentServiceImpl(
            CheckAuthorization checkAuthorization, CommentMapper commentMapper,
            CommentRepository commentRepository,
            PostRepository postRepository, AuthorRepository userRepository) {
        this.checkAuthorization = checkAuthorization;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        validateAndSanitizeRoleName("PostId", requestDTO.getPostId());
        validateAndSanitizeRoleName("Content", requestDTO.getContent());

        SecurityContext context = SecurityContextHolder.getContext();
        String usernameOrEmail = context.getAuthentication().getName();

        Author user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
                () -> new ResourceNotFoundException("Author", "usernameOrEmail", context.getAuthentication().getName())
        );

        Post post = postRepository.findById(UUID.fromString(requestDTO.getPostId())).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", requestDTO.getPostId())
        );

        try {
            return commentMapper.commentToCommentResponseDTO(
                    commentRepository.save(new Comment(requestDTO.getContent(), post, user))
            );
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }

    @Override
    public List<CommentResponseDTO> getAllComments(String postId) {
        List<Comment> comments = commentRepository.findAll(UUID.fromString(postId));

        return comments
                .stream()
                .map(commentMapper::commentToCommentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Comment findById(String id) {
        validateAndSanitizeRoleName("CommentId", id);
        return commentRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id)
        );
    }

    @Override
    public MessageResponseDTO deleteById(String id) throws AccessDeniedException {
        validateAndSanitizeRoleName("CommentId", id);
        Comment comment = getComment(id);

        commentRepository.deleteById(comment.getId());
        return new MessageResponseDTO("Comment with id : " + id + " is deleted.");

    }

    @Override
    public CommentResponseDTO updateComment(String id, String content) throws AccessDeniedException {
        validateAndSanitizeRoleName("CommentId", id);
        validateAndSanitizeRoleName("Content", content);

        Comment comment = getComment(id);

        comment.setContent(content);

        return commentMapper.commentToCommentResponseDTO(commentRepository.save(comment));
    }

    public Comment getComment(String id) throws AccessDeniedException {

        Comment comment = commentRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id)
        );

        checkAuthorization.checkUser(comment.getAuthor());

        return comment;
    }

    private void validateAndSanitizeRoleName(String fieldName, String fieldValue) {
        Assert.notNull(fieldValue, "Provide a valid " + fieldName + " , please.");
        if (fieldValue.isEmpty() || fieldValue.isBlank()) {
            throw new IllegalArgumentException("Provide a valid " + fieldName + " , please.");
        }
    }
}
