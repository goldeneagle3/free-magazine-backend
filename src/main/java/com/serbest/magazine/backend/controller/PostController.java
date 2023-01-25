package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.post.*;
import com.serbest.magazine.backend.service.PostService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostCreateResponseDTO> createPost(@Valid @ModelAttribute PostRequestDTO requestDTO) throws IOException {
        return ResponseEntity.ok(postService.createPost(requestDTO));
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PostMapping(value = "/editor/createPost", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostCreateResponseDTO> createPost(@Valid @ModelAttribute PostCreateEditorRequestDTO requestDTO) throws IOException {
        return ResponseEntity.ok(postService.createPostEditor(requestDTO));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostResponseDTO>> getAllPost() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping(value = "/firstFivePosts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FirstFivePostsListDTO>> getFirstFivePosts() {
        return ResponseEntity.ok(postService.getFirstFivePosts());
    }

    @GetMapping(value = "/fourPostsForTop",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MainPagePostsListDTO>> getFourPostsForTop() {
        return ResponseEntity.ok(postService.getFourPostsForTop());
    }

    @GetMapping(value = "/mainPagePosts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MainPagePostsListDTO>> getPostsForMainPage() {
        return ResponseEntity.ok(postService.getPostsForMainPage());
    }

    @GetMapping("/getSinglePostBy/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable String postId){
        return ResponseEntity.ok(postService.findById(postId));
    }

    @GetMapping("/getPostsByAuthor/{username}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(@PathVariable String username){
        return ResponseEntity.ok(postService.findByUsername(username));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDeactivatedPosts")
    public ResponseEntity<List<DeactivatedPostApiResponseDTO>> getDeactivatedPosts(){
        return ResponseEntity.ok(postService.getDeactivatedPost());
    }

    @PutMapping("/deactivatePost/{id}")
    public ResponseEntity<PostResponseDTO> deactivatePost(@PathVariable String id) throws AccessDeniedException {
        return ResponseEntity.ok(postService.deactivatePost(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/activatePost/{id}")
    public ResponseEntity<PostResponseDTO> activatePost(@PathVariable String id){
        return ResponseEntity.ok(postService.activatePost(id));
    }

    @PutMapping(value = "/updatePost/{id}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable String id,@ModelAttribute PostUpdateRequestDTO requestDTO)
            throws IOException {
        return ResponseEntity.ok(postService.updatePost(id,requestDTO));
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PutMapping(value = "/editor/updatePost/{id}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostResponseDTO> updatePostForEditor(@PathVariable String id,@ModelAttribute PostUpdateEditorRequestDTO requestDTO)
            throws IOException {
        return ResponseEntity.ok(postService.updatePostEditor(id,requestDTO));
    }

    @GetMapping("/getPostByCategory/{category}")
    public ResponseEntity<List<PostResponseDTO>> getAllPostByCategory(@PathVariable String category){
        return ResponseEntity.ok(postService.getPostsByCategory(category));
    }

    @GetMapping("/randomThree")
    public ResponseEntity<List<PostResponseDTO>> getThreeByRandomPosts(){
        return ResponseEntity.ok(postService.getRandomThreePost());
    }

    @GetMapping("/countsByCategory/{categoryName}")
    public ResponseEntity<Integer> countsByCategoryName(@PathVariable String categoryName){
        return ResponseEntity.ok(postService.countsByCategoryName(categoryName));
    }

}
