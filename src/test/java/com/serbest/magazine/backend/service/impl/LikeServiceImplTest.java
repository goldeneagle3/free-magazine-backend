package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.like.LikeRequestDTO;
import com.serbest.magazine.backend.dto.like.LikeResponseDTO;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Comment;
import com.serbest.magazine.backend.entity.Like;
import com.serbest.magazine.backend.entity.Post;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.repository.AuthorRepository;
import com.serbest.magazine.backend.repository.CommentRepository;
import com.serbest.magazine.backend.repository.LikeRepository;
import com.serbest.magazine.backend.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @InjectMocks
    LikeServiceImpl likeService;

    @Mock
    LikeRepository likeRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    CommentRepository commentRepository;

    @Test
    public void test_like_withSuccess() throws AccessDeniedException {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        UUID likeId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();

        Author authorMock = mock(Author.class);
        Post postMock = mock(Post.class);
        Like likeMock = mock(Like.class);

        LikeRequestDTO requestDTO = new LikeRequestDTO(postId.toString(), null);

        when(authorRepository.findByUsernameOrEmail("testUser", "testUser")).thenReturn(Optional.of(authorMock));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postMock));
        when(likeMock.getId()).thenReturn(likeId);

        when(likeRepository.save(any(Like.class))).thenReturn(likeMock);

        LikeResponseDTO responseDTO = likeService.like(requestDTO);

        assertNotNull(responseDTO.getLikeId());

    }

    @Test
    public void test_like_withNothingProvided() {
        assertThrows(
                CustomApplicationException.class,
                () -> likeService.like(new LikeRequestDTO())
        );
    }

    @Test
    public void test_like_withBothPostAndCommentProvided() {
        assertThrows(
                CustomApplicationException.class,
                () -> likeService.like(new LikeRequestDTO(
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString()))
        );
    }

    @Test
    public void test_like_withWrongPostId() {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        UUID postId = UUID.randomUUID();

        Author authorMock = mock(Author.class);
        LikeRequestDTO requestDTO = new LikeRequestDTO(postId.toString(), null);

        when(authorRepository.findByUsernameOrEmail("testUser", "testUser")).thenReturn(Optional.of(authorMock));
        assertThrows(
                ResourceNotFoundException.class,
                () -> likeService.like(requestDTO)
        );
    }

    @Test
    public void test_like_withUserNotFound() {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        UUID postId = UUID.randomUUID();

        LikeRequestDTO requestDTO = new LikeRequestDTO(postId.toString(), null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> likeService.like(requestDTO)
        );

    }

    @Test
    public void test_likedUsersByPost_withSuccess() {
        UUID postId = UUID.randomUUID();

        when(likeRepository.findLikedAuthorsByPostId(postId)).thenReturn(List.of("Ensar", "Ezber"));

        List<String> users = likeService.likedUsersByPost(postId.toString());

        assertEquals(2, users.size());
        assertEquals("Ensar", users.get(0));
    }

    @Test
    public void test_likedUsersByComment_withSuccess() {
        UUID commentId = UUID.randomUUID();

        when(likeRepository.findLikedAuthorsByCommentId(commentId))
                .thenReturn(List.of("Ensar", "Ezber"));

        List<String> users = likeService.likedUsersByComment(commentId.toString());

        assertEquals(2, users.size());
        assertEquals("Ensar", users.get(0));
    }

}