package com.serbest.magazine.backend.service.impl;

import com.google.common.base.Strings;

import com.serbest.magazine.backend.dto.post.*;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Category;
import com.serbest.magazine.backend.entity.Post;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.PostMapper;
import com.serbest.magazine.backend.repository.AuthorRepository;
import com.serbest.magazine.backend.repository.CategoryRepository;
import com.serbest.magazine.backend.repository.PostRepository;
import com.serbest.magazine.backend.service.PostService;
import com.serbest.magazine.backend.security.CheckAuthorization;
import com.serbest.magazine.backend.util.UploadImage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final Path root = Paths.get("uploads");
    private final CheckAuthorization checkAuthorization;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AuthorRepository userRepository;

    public PostServiceImpl(CheckAuthorization checkAuthorization, CategoryRepository categoryRepository,
                           PostRepository postRepository, PostMapper postMapper, AuthorRepository userRepository) {
        this.checkAuthorization = checkAuthorization;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PostCreateResponseDTO createPost(PostRequestDTO requestDTO) throws IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        String usernameOrEmail = context.getAuthentication().getName();

        Optional<Author> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (Strings.isNullOrEmpty(user.get().getEmail())) {
            throw new ResourceNotFoundException("Author", "emailOrUsername", usernameOrEmail);
        }

        Optional<Category> category = categoryRepository
                .findByName(requestDTO.getCategory());

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category", "name", requestDTO.getCategory());
        }

        Post post = null;
        try {
            post = postMapper.postRequestDTOToPost(requestDTO);
            post.setAuthor(user.get());
            post.setCategory(category.get());

            return postMapper.postToPostCreateResponseDTO(postRepository.save(post));
        } catch (IOException e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public PostCreateResponseDTO createPostEditor(PostCreateEditorRequestDTO requestDTO) throws IOException {

        Author user = userRepository.findByUsername(requestDTO.getAuthor()).orElseThrow(
                () -> new ResourceNotFoundException("Author", "username", requestDTO.getAuthor())
        );

        Category category = categoryRepository.findByName(requestDTO.getCategory()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "name", requestDTO.getCategory())
        );


        Post post = null;
        try {
            post = postMapper.postCreateEditorRequestDTOToPost(requestDTO);
            post.setAuthor(user);
            post.setCategory(category);

            return postMapper.postToPostCreateResponseDTO(postRepository.save(post));
        } catch (IOException e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findByActiveTrueOrderByCreateDateTimeDesc();

        return posts
                .stream()
                .map(postMapper::postToPostResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FirstFivePostsListDTO> getFirstFivePosts() {
        List<Post> posts = postRepository.findFirstFiveActiveTrueByCreateDateTime();

        return posts
                .stream()
                .map(postMapper::postToFirstFivePostsListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MainPagePostsListDTO> getFourPostsForTop() {
        List<Post> posts = postRepository.findFourPostsActiveTrueByCreateDateTime();

        return posts
                .stream()
                .map(postMapper::postToMainPagePostsListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MainPagePostsListDTO> getPostsForMainPage() {
        List<Post> posts = postRepository.findFifteenActiveTrueByCreateDateTimeOffset5();

        return posts
                .stream()
                .map(postMapper::postToMainPagePostsListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeactivatedPostApiResponseDTO> getDeactivatedPost() {
        List<Post> posts = postRepository.findByActiveFalseOrderByCreateDateTimeDesc();

        return posts.stream().map(postMapper::postToDeactivatedPostApiResponseDTO).collect(Collectors.toList());
    }

    @Override
    public PostResponseDTO findById(String id) {
        Post post = postRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );

        return postMapper.postToPostResponseDTO(post);
    }

    @Override
    public PostResponseDTO deactivatePost(String id) throws AccessDeniedException {
        Post post = getPost(id);
        post.setActive(false);

        try {
            return postMapper.postToPostResponseDTO(postRepository.save(post));
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public PostResponseDTO activatePost(String id){
        Post post = postRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",id)
        );
        post.setActive(true);

        try {
            return postMapper.postToPostResponseDTO(postRepository.save(post));
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public PostResponseDTO updatePost(String id, PostUpdateRequestDTO requestDTO) throws IOException {
        Post post = getPost(id);

        Category category = categoryRepository.findByName(requestDTO.getCategory()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "name", requestDTO.getCategory())
        );

        post.setCategory(category);
        post.setTitle(requestDTO.getTitle());
        post.setSubtitle(requestDTO.getSubtitle());
        post.setContent(requestDTO.getContent());
        if (!requestDTO.getImageProtect()) {
            post.setPostImage(UploadImage.uploadImage(requestDTO.getImage()));
        }
        try {
            return postMapper.postToPostResponseDTO(postRepository.save(post));
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public PostResponseDTO updatePostEditor(String id, PostUpdateEditorRequestDTO requestDTO) throws IOException {
        Post post = postRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );

        Category category = categoryRepository.findByName(requestDTO.getCategory()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "name", requestDTO.getCategory())
        );

        post.setCategory(category);
        post.setTitle(requestDTO.getTitle());
        post.setSubtitle(requestDTO.getSubtitle());
        post.setContent(requestDTO.getContent());
        if (!requestDTO.getImageProtect()) {
            post.setPostImage(UploadImage.uploadImage(requestDTO.getImage()));
        }
        try {
            return postMapper.postToPostResponseDTO(postRepository.save(post));
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<PostResponseDTO> getRandomThreePost() {
        List<Post> posts = postRepository.findThreeActiveTrueByRandom();
        return posts
                .stream()
                .map(postMapper::postToPostResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPostsByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(
                () -> new ResourceNotFoundException("Category", "name", categoryName)
        );
        List<Post> posts = postRepository.findAllByCategoryNameAndActiveTrueOrderByCreateDateTimeDesc(categoryName);
        return posts
                .stream()
                .map(postMapper::postToPostResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> findByUsername(String username) {
        List<Post> posts = postRepository.findAllByAuthorUsernameAndActiveTrueOrderByCreateDateTimeDesc(username);

        return posts
                .stream()
                .map(postMapper::postToPostResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer countsByCategoryName(String categoryName) {
        return postRepository.countByCategoryNameAndActiveTrue(categoryName);
    }

    private Post getPost(String id) throws AccessDeniedException {

        Post post = postRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", Long.parseLong(id))
        );
        checkAuthorization.checkUser(post.getAuthor());

        return post;
    }

}
