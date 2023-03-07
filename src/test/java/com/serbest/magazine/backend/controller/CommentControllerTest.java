package com.serbest.magazine.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.comment.CommentRequestDTO;
import com.serbest.magazine.backend.dto.comment.CommentResponseDTO;
import com.serbest.magazine.backend.dto.comment.CommentUpdateRequestDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.service.CommentService;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void RA_test_createComment_shouldAllowCommentCreationWithAuthentication() {
        UUID postId = UUID.randomUUID();
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("TestContent", postId.toString());
        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .commentId(UUID.randomUUID())
                .content(commentRequestDTO.getContent())
                .username("ensar")
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();

        Mockito.when(commentService.createComment(commentRequestDTO)).thenReturn(responseDTO);

        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(commentRequestDTO)
                .when()
                .post("/api/comments")

                .then()
                .statusCode(201);
    }

    @Test
    public void RA_test_createComment_shouldReturn401AuthenticationError() {
        RestAssuredMockMvc
                .given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("test")
                .when()
                .post("/api/comments")
                .then()
                .statusCode(401);
    }

    @Test
    public void RA_test_createComment_shouldReturn400MissingParamBodyError() {
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("", UUID.randomUUID().toString());

        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(commentRequestDTO)
                .when()
                .post("/api/comments")
                .then()
                .statusCode(400);
    }

    @Test
    public void RA_test_createComment_shouldReturn400MissingPostIdError() {
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("test", "");

        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(commentRequestDTO)
                .when()
                .post("/api/comments")
                .then()
                .statusCode(400);
    }

    @Test
    public void RA_test_getAllComments_shouldAllowCommentsFetchingWithAuthentication() {
        UUID postId = UUID.randomUUID();
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("TestContent", postId.toString());
        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .commentId(UUID.randomUUID())
                .content(commentRequestDTO.getContent())
                .username("ensar")
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();

        Mockito.when(commentService.getAllComments(postId.toString())).thenReturn(List.of(responseDTO));

        RestAssuredMockMvc
                .given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/comments/byPost/" + postId)
                .then()
                .statusCode(200)
                .body("$.size()", Matchers.equalTo(1));
    }

    @Test
    public void RA_test_deleteCommentById_shouldAllowDeleteCommentWithAuthentication() throws AccessDeniedException {
        UUID postId = UUID.randomUUID();
        MessageResponseDTO responseDTO = new MessageResponseDTO("Comment with id : " + postId + " is deleted.");
        Mockito.when(commentService.deleteById(postId.toString())).thenReturn(responseDTO);

        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/comments/" + postId)
                .then()
                .statusCode(200);

    }

    @Test
    public void RA_test_deleteCommentById_shouldReturn401AuthenticationError() {
        UUID postId = UUID.randomUUID();

        RestAssuredMockMvc
                .given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/comments/" + postId)
                .then()
                .statusCode(401);

    }

    @Test
    public void RA_test_updateComment_shouldAllowCommentsFetchingWithAuthentication() throws AccessDeniedException {
        UUID commentId = UUID.randomUUID();
        CommentUpdateRequestDTO commentRequestDTO = new CommentUpdateRequestDTO("TestUpdatedContent");
        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .commentId(commentId)
                .content(commentRequestDTO.getContent())
                .username("ensar")
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();

        Mockito.when(commentService.updateComment(commentId.toString(),commentRequestDTO.getContent()))
                .thenReturn(responseDTO);

        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(commentRequestDTO)
                .when()
                .put("/api/comments/updateComment/"+ commentId)

                .then()
                .statusCode(200);
    }

    @Test
    public void RA_test_updateComment_shouldReturn401AuthenticationError() {

        RestAssuredMockMvc
                .given()
                .auth().none()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("test")
                .when()
                .post("/api/comments/updateComment/" + "testId")
                .then()
                .statusCode(401);
    }

    @Test
    public void RA_test_updateComment_shouldReturn400WhenMissingParams() {
        UUID commentId = UUID.randomUUID();
        CommentUpdateRequestDTO commentRequestDTO = new CommentUpdateRequestDTO("");
        RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("ensar").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(commentRequestDTO)
                .when()
                .put("/api/comments/updateComment/"+ commentId)

                .then()
                .statusCode(200);
    }
}