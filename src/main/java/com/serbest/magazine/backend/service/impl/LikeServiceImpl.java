package com.serbest.magazine.backend.service.impl;

import com.google.common.base.Strings;
import com.serbest.magazine.backend.dto.like.LikeRequestDTO;
import com.serbest.magazine.backend.dto.like.LikeResponseDTO;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Comment;
import com.serbest.magazine.backend.entity.Like;
import com.serbest.magazine.backend.entity.Post;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.repository.AuthorRepository;
import com.serbest.magazine.backend.repository.LikeRepository;
import com.serbest.magazine.backend.repository.PostRepository;
import com.serbest.magazine.backend.service.CommentService;
import com.serbest.magazine.backend.service.LikeService;
import com.serbest.magazine.backend.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final LikeRepository likeRepository;
    private final AuthorRepository authorRepository;

    public LikeServiceImpl(PostRepository postRepository, CommentService commentService, LikeRepository likeRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.likeRepository = likeRepository;
        this.authorRepository = authorRepository;
    }

    /// Added CHECK of COALESCE((post)::int::boolean::int) + COALESCE((comment)::int::boolean::int) = 1;
    @Override
    public LikeResponseDTO like(LikeRequestDTO requestDTO) throws AccessDeniedException {
        if (Strings.isNullOrEmpty(requestDTO.getPostId()) && Strings.isNullOrEmpty(requestDTO.getCommentId())) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST,
                    "Post or Comment not found!");
        }
        // (COALESCE(post::integer::boolean::integer, 0) + COALESCE(comment::integer::boolean::integer, 0)) = 1

        if (!Strings.isNullOrEmpty(requestDTO.getPostId()) && !Strings.isNullOrEmpty(requestDTO.getCommentId())) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST,
                    "You can not like both post and comment at the same time!");
        }

        SecurityContext context = SecurityContextHolder.getContext();
        String usernameOrEmail = context.getAuthentication().getName();

        if (Strings.isNullOrEmpty(usernameOrEmail)) {
            throw new AccessDeniedException("You are not allowed to do that!");
        }

        Author user = authorRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
                () -> new ResourceNotFoundException("Author", "username or email", usernameOrEmail)
        );

        Like like = null;

        if (!Strings.isNullOrEmpty(requestDTO.getPostId())) {

            Post post = postRepository.findById(UUID.fromString(requestDTO.getPostId())).orElseThrow(
                    () -> new ResourceNotFoundException("Post", "id", requestDTO.getPostId())
            );

            like = likeRepository.findByAuthorAndPost(user, post);
            if (like != null) {
                likeRepository.deleteByPostAndAuthor(post.getPostId(), user.getId());
            } else {
                like = likeRepository.save(new Like(post, null, user));
            }

        } else if (!Strings.isNullOrEmpty(requestDTO.getCommentId())) {

            Comment comment = commentService.findById(requestDTO.getCommentId());

            like = likeRepository.findByAuthorAndComment(user, comment);
            if (like != null) {
                likeRepository.deleteByCommentAndAuthor(comment.getId(), user.getId());
            } else {
                like = likeRepository.save(new Like(null, comment, user));
            }
        }

        return new LikeResponseDTO(like.getId());
    }

    @Override
    public List<String> likedUsersByPost(String postId) {
        return likeRepository.findLikedAuthorsByPostId(UUID.fromString(postId));
    }

    @Override
    public List<String> likedUsersByComment(String commentId) {
        return likeRepository.findLikedAuthorsByCommentId(UUID.fromString(commentId));
    }
}
