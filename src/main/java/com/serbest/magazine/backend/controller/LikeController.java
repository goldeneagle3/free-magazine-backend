package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.like.LikeRequestDTO;
import com.serbest.magazine.backend.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
@CrossOrigin(origins = {"http://localhost:3000", "https://magazine-app.netlify.app"}, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping
    public void likeFunctionality(@RequestBody LikeRequestDTO requestBody) throws AccessDeniedException {
        likeService.like(requestBody);
    }

    @GetMapping("/likedUsersByPost/{postId}")
    public ResponseEntity<List<String>> likedUsersByPost(@PathVariable String postId){
        return ResponseEntity.ok(likeService.likedUsersByPost(postId));
    }

    @GetMapping("/likedUsersByComment/{commentId}")
    public ResponseEntity<List<String>> likedUsersByComment(@PathVariable String commentId){
        return ResponseEntity.ok(likeService.likedUsersByComment(commentId));
    }
}
