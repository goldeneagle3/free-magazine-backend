package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.like.LikeRequestDTO;
import com.serbest.magazine.backend.dto.like.LikeResponseDTO;
import com.serbest.magazine.backend.entity.*;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.repository.*;
import com.serbest.magazine.backend.service.CommentService;
import com.serbest.magazine.backend.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class LikeServiceImplIntegrationTest {

    @Autowired
    LikeService likeService;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuthorRepository authorRepository;

    UUID authorId;

    UUID postId;

    UUID commentId;

    @BeforeEach
    void createNecessaryModels() {

        Author testUser = Author.Builder.newBuilder()
                .username("testUser")
                .password("testpassword")
                .email("test@email.com")
                .active(true)
                .createDateTime(LocalDateTime.now())
                .build();

        Author author = authorRepository.save(testUser);

        authorId = author.getId();

        Category category = categoryRepository.save(new Category("siyaset"));

        Post testPost = Post.Builder.newBuilder()
                .postId(this.postId)
                .title("testTitle")
                .author(author)
                .category(category)
                .active(true)
                .content("testContent")
                .subtitle("testSubtitle")
                .build();

        testPost.setCreateDateTime(LocalDateTime.now());
        testPost.setUpdateDateTime(LocalDateTime.now());

        Post post = postRepository.save(testPost);

        this.postId = post.getPostId();

        Comment newComment = new Comment("Content", post, author);
        Comment comment = commentRepository.save(newComment);

        this.commentId = comment.getId();
    }


    @Test
    public void testIntegration_like_withSuccess() throws AccessDeniedException {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        LikeRequestDTO requestDTO = new LikeRequestDTO(this.postId.toString(), null);

        LikeResponseDTO responseDTO = likeService.like(requestDTO);

        assertNotNull(responseDTO.getLikeId());
    }

    @Tag("NeedCoalesce")
    @Test
    public void testIntegration_like_withNoneLiked() {
        assertThrows(
                CustomApplicationException.class,
                () -> likeService.like(new LikeRequestDTO())
        );
    }

    @Tag("NeedCoalesce")
    @Test
    public void testIntegration_like_withBothLiked() {
        assertThrows(
                CustomApplicationException.class,
                () -> likeService.like(new LikeRequestDTO(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
        );
    }

    @Test
    public void testIntegration_likedUsersByPostId_withSuccess() {
        Post post = postRepository.findById(postId).get();
        Author author = authorRepository.findById(authorId).get();

        likeRepository.save(new Like(post, null, author));

        List<String> users = likeService.likedUsersByPost(postId.toString());

        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0));

    }

    @Test
    public void testIntegration_likedUsersByComment_withSuccess() {
        Comment comment = commentRepository.findById(commentId).get();
        Author author = authorRepository.findById(authorId).get();

        likeRepository.save(new Like(null, comment, author));

        List<String> users = likeService.likedUsersByComment(commentId.toString());

        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0));

    }

}