package com.serbest.magazine.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.comment.CommentRequestDTO;
import com.serbest.magazine.backend.dto.comment.CommentResponseDTO;
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

import java.time.LocalDateTime;
import java.util.Arrays;
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
    public void RA_test_createComment_shouldAllowCommentCreationWithAuthentication(){
        UUID postId = UUID.randomUUID();
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("TestContent",postId.toString());
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
    public void RA_test_createComment_shouldReturn401AuthenticationError(){
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
    public void RA_test_createComment_shouldReturn400MissingParamBodyError(){
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO("",UUID.randomUUID().toString());

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
}