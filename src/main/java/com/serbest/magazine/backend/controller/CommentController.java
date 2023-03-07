package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.comment.CommentRequestDTO;
import com.serbest.magazine.backend.dto.comment.CommentResponseDTO;
import com.serbest.magazine.backend.dto.comment.CommentUpdateRequestDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://magazine-app.netlify.app"}, maxAge = 5600, allowCredentials="true")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(requestDTO));
    }

    @GetMapping("/byPost/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments(@PathVariable String postId){
        return ResponseEntity.ok(commentService.getAllComments(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteCommentById(@PathVariable String id) throws AccessDeniedException {
        return ResponseEntity.ok(commentService.deleteById(id));
    }

    @PutMapping("/updateComment/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable String id, @Valid @RequestBody CommentUpdateRequestDTO requestDTO) throws AccessDeniedException {
        return ResponseEntity.ok(commentService.updateComment(id,requestDTO.getContent()));
    }
}
