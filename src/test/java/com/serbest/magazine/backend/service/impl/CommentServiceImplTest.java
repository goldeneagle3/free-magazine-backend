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
import com.serbest.magazine.backend.security.CheckAuthorization;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    CommentServiceImpl commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    CommentMapper commentMapper;

    @Mock
    PostRepository postRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    CheckAuthorization checkAuthorization;

    @Test
    public void test_createComment_withSuccess() {
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();
        CommentRequestDTO commentRequestDTO =
                new CommentRequestDTO("TestContent", postId.toString());

        Comment commentMock = Mockito.mock(Comment.class);

        when(commentMock.getId()).thenReturn(commentId);
        when(commentMock.getContent()).thenReturn("TestContent");

        when(commentRepository.save(any(Comment.class))).thenReturn(commentMock);
        Comment comment = commentRepository.save(new Comment("TestContent"));

        assertEquals(comment.getId(), commentId);
        assertEquals(comment.getContent(), commentRequestDTO.getContent());
    }

    @Test
    public void test_createComment_withMissingArg() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();

        assertThrows(
                IllegalArgumentException.class,
                () -> commentService.createComment(requestDTO)
        );
    }

    @Test
    public void test_createComment_withNullInput() {
        CommentRequestDTO requestDTO = new CommentRequestDTO(null, null);

        assertThrows(
                IllegalArgumentException.class,
                () -> commentService.createComment(requestDTO)
        );
    }

    @Disabled("Need checking")
    @Test
    public void test_createComment_withError() {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Author mockAuthor = Mockito.mock(Author.class);
        when(authorRepository.findByUsernameOrEmail(null, null))
                .thenReturn(Optional.of(mockAuthor));

        UUID postId = UUID.randomUUID();
        Post postMock = Mockito.mock(Post.class);

        when(postMock.getPostId()).thenReturn(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(postMock));

        // No Save Instruction.
        assertThrows(
                CustomApplicationException.class,
                () -> commentService.createComment(
                        new CommentRequestDTO("TestContent", postMock.getPostId().toString()))
        );
    }

    @Test
    public void test_getAllComments_withSuccess() {
        UUID postId = UUID.randomUUID();
        Comment comment1 = new Comment("TestContent1");
        Comment comment2 = new Comment("TestContent2");

        when(commentRepository.findAll(postId)).thenReturn(List.of(comment1, comment2));
        List<CommentResponseDTO> responseDTOS = commentService.getAllComments(postId.toString());

        assertEquals(responseDTOS.size(), 2);
    }

    @Test
    @Tag("UnitTest, Needs revision on service method")
    public void test_findById_withSuccess() {
        UUID commentId = UUID.randomUUID();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment("testContent")));

        Comment responseDTO = commentService.findById(commentId.toString());

        assertEquals(responseDTO.getContent(), "testContent");


    }

    @Test
    public void test_findById_withMissingCommentIdParam() {
        assertThrows(
                IllegalArgumentException.class,
                () -> commentService.findById("")
        );
    }

    @Test
    public void test_deleteById_withSuccess() throws AccessDeniedException {

        UUID commentId = UUID.randomUUID();
        Comment mockComment = Mockito.mock(Comment.class);

        when(mockComment.getId()).thenReturn(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        MessageResponseDTO responseDTO = commentService.deleteById(commentId.toString());

        assertEquals(
                responseDTO.getMessage(),
                "Comment with id : " + commentId + " is deleted."
        );
    }

    @Test
    public void test_deleteById_withoutProvidedCommentId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> commentService.deleteById("")
        );
    }

    @Test
    public void test_deleteById_withResourceNotFound() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> commentService.deleteById(UUID.randomUUID().toString())
        );
    }

    @Test
    public void test_updateComment_withSuccess() throws AccessDeniedException {
        UUID randomId = UUID.randomUUID();

        Comment comment = new Comment("Testtt");
        CommentResponseDTO commentResponseDTO = CommentResponseDTO.builder()
                .content("UpdatedComment")
                .username("test")
                .commentId(randomId)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.MAX)
                .build();

        doReturn(Optional.of(comment)).when(commentRepository).findById(randomId);

        comment.setId(randomId);
        comment.setContent("UpdatedComment");

        when(commentMapper.commentToCommentResponseDTO(any(Comment.class))).thenReturn(commentResponseDTO);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        CommentResponseDTO responseDTO = commentService.updateComment(randomId.toString(), "UpdatedComment");

        assertEquals(responseDTO.getContent(), "UpdatedComment");

    }

    @Test
    public void test_updateComment_withResourceNotFound() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> commentService.updateComment(UUID.randomUUID().toString(), "UpdatedComment")
        );

    }

    @Test
    public void test_updateComment_withMissingCommentId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> commentService.updateComment("", "UpdatedComment")
        );

    }

    @Test
    public void test_updateComment_withMissingReqBody() {
        assertThrows(
                IllegalArgumentException.class,
                () -> commentService.updateComment(UUID.randomUUID().toString(), "")
        );

    }


}