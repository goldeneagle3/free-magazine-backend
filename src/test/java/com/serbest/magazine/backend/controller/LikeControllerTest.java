package com.serbest.magazine.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serbest.magazine.backend.dto.comment.CommentRequestDTO;
import com.serbest.magazine.backend.dto.comment.CommentResponseDTO;
import com.serbest.magazine.backend.dto.like.LikeRequestDTO;
import com.serbest.magazine.backend.dto.like.LikeResponseDTO;
import com.serbest.magazine.backend.service.LikeService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void RA_test_likeFunctionality_shouldAllowCommentCreationWithAuthentication() throws AccessDeniedException {
        UUID postId = UUID.randomUUID();
        LikeRequestDTO requestDTO = new LikeRequestDTO( postId.toString(),null);
        LikeResponseDTO responseDTO = new LikeResponseDTO(UUID.randomUUID());

        Mockito.when(likeService.like(requestDTO)).thenReturn(responseDTO);

        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .when()
                .put("/api/likes")
                .then()
                .statusCode(200);
    }

    @Test
    public void RA_test_likeFunctionality_shouldReturn401AuthenticationError() {
        RestAssuredMockMvc
                .given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("test")
                .when()
                .put("/api/likes")
                .then()
                .statusCode(401);
    }

    @Test
    public void RA_test_createComment_shouldReturn400MissingParamBodyError() {
        LikeRequestDTO requestDTO = new LikeRequestDTO( null,null);

        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDTO)
                .when()
                .put("/api/likes")
                .then()
                .statusCode(400);
    }

    @Test
    public void RA_test_likedUsersByPost_shouldAllowCommentsFetchingWithoutAuthentication(){
        UUID postId = UUID.randomUUID();

        Mockito.when(likeService.likedUsersByPost(postId.toString())).thenReturn(List.of("John","Jack"));

        RestAssuredMockMvc
                .given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/likes/likedUsersByPost/"+postId)
                .then()
                .statusCode(200)
                .body("$", Matchers.equalTo(List.of("John","Jack")));

    }

    @Test
    public void RA_test_likedUsersByComment_shouldAllowCommentsFetchingWithoutAuthentication(){
        UUID commentId = UUID.randomUUID();

        Mockito.when(likeService.likedUsersByComment(commentId.toString())).thenReturn(List.of("John","Jack"));

        RestAssuredMockMvc
                .given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/likes/likedUsersByComment/"+commentId)
                .then()
                .statusCode(200)
                .body("$", Matchers.equalTo(List.of("John","Jack")));

    }
}