package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.post.*;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Category;
import com.serbest.magazine.backend.entity.Post;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.PostMapper;
import com.serbest.magazine.backend.repository.AuthorRepository;
import com.serbest.magazine.backend.repository.CategoryRepository;
import com.serbest.magazine.backend.repository.PostRepository;
import com.serbest.magazine.backend.security.CheckAuthorization;
import com.serbest.magazine.backend.service.ImageModelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    PostServiceImpl postService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    AuthorRepository authorRepository;

    @Mock
    ImageModelService imageModelService;

    @Mock
    PostMapper postMapper;

    @Mock
    CheckAuthorization checkAuthorization;

    @Test
    public void test_createPost_withSuccess() throws IOException {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        when(authentication.getName()).thenReturn("test");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostRequestDTO requestDTO =
                new PostRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile);

        Author author = mock(Author.class);
        Category category = mock(Category.class);
        Post post = mock(Post.class);

        when(authorRepository.findByUsernameOrEmail("test", "test")).thenReturn(Optional.of(author));
        when(categoryRepository.findByName("Siyaset")).thenReturn(Optional.of(category));

        when(postMapper.postRequestDTOToPost(requestDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.postToPostCreateResponseDTO(post)).thenReturn(PostCreateResponseDTO.builder()
                .id(UUID.randomUUID())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .build());

        PostCreateResponseDTO responseDTO = postService.createPost(requestDTO);

        assertEquals(requestDTO.getTitle(), responseDTO.getTitle());


    }

    @Test
    public void test_createPost_withMissingParam() {
        assertThrows(
                IllegalArgumentException.class,
                () -> postService.createPost(new PostRequestDTO("", "", "", "", null))
        );
    }

    @Test
    public void test_createPost_authorNotFound() throws IOException {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        when(authentication.getName()).thenReturn("test");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostRequestDTO requestDTO =
                new PostRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile);

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.createPost(requestDTO)
        );

    }

    @Test
    public void test_createPost_categoryNotFound() throws IOException {
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mockito.whens() for your authorization object
        when(authentication.getName()).thenReturn("test");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostRequestDTO requestDTO =
                new PostRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile);

        Author author = mock(Author.class);

        when(authorRepository.findByUsernameOrEmail("test", "test")).thenReturn(Optional.of(author));

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.createPost(requestDTO)
        );

    }

    @Test
    public void test_createPostEditor_withSuccess() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostCreateEditorRequestDTO requestDTO =
                new PostCreateEditorRequestDTO("Test Title", null, "test", "Test Content", "Siyaset", multipartFile);

        Author author = mock(Author.class);
        Category category = mock(Category.class);
        Post post = mock(Post.class);

        when(authorRepository.findByUsername("test")).thenReturn(Optional.of(author));
        when(categoryRepository.findByName("Siyaset")).thenReturn(Optional.of(category));

        when(postMapper.postCreateEditorRequestDTOToPost(requestDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.postToPostCreateResponseDTO(post)).thenReturn(PostCreateResponseDTO.builder()
                .id(UUID.randomUUID())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .build());

        PostCreateResponseDTO responseDTO = postService.createPostEditor(requestDTO);

        assertEquals(requestDTO.getTitle(), responseDTO.getTitle());


    }

    @Test
    public void test_createPostEditor_withMissingParam() {
        assertThrows(
                IllegalArgumentException.class,
                () -> postService.createPostEditor(new PostCreateEditorRequestDTO("", "", "", "", "", null))
        );
    }

    @Test
    public void test_createPostEditor_authorNotFound() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostCreateEditorRequestDTO requestDTO =
                new PostCreateEditorRequestDTO("Test Title", null, "test", "Test Content", "Siyaset", multipartFile);

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.createPostEditor(requestDTO)
        );

    }

    @Test
    public void test_createPostEditor_categoryNotFound() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostCreateEditorRequestDTO requestDTO =
                new PostCreateEditorRequestDTO("Test Title", null, "test", "Test Content", "Siyaset", multipartFile);

        Author author = mock(Author.class);

        when(authorRepository.findByUsername("test")).thenReturn(Optional.of(author));

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.createPostEditor(requestDTO)
        );

    }

    @Test
    public void test_getAllPosts_withSuccess() {
        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(postRepository.findByActiveTrueOrderByCreateDateTimeDesc()).thenReturn(List.of(post));

        List<PostResponseDTO> responseDTOS = postService.getAllPosts();

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_getFirstFivePosts_withSuccess() {
        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(postRepository.findFirstFiveActiveTrueByCreateDateTime()).thenReturn(List.of(post));

        List<FirstFivePostsListDTO> responseDTOS = postService.getFirstFivePosts();

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_getFourPostsForTop_withSuccess() {
        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(postRepository.findFourPostsActiveTrueByCreateDateTime()).thenReturn(List.of(post));

        List<MainPagePostsListDTO> responseDTOS = postService.getFourPostsForTop();

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_getPostsForMainPage_withSuccess() {
        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(postRepository.findFifteenActiveTrueByCreateDateTimeOffset5()).thenReturn(List.of(post));

        List<MainPagePostsListDTO> responseDTOS = postService.getPostsForMainPage();

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_getDeactivatedPost_withSuccess() {
        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .active(false)
                .build();

        when(postRepository.findByActiveFalseOrderByCreateDateTimeDesc()).thenReturn(List.of(post));

        List<DeactivatedPostApiResponseDTO> responseDTOS = postService.getDeactivatedPost();

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_findById_withSuccess() {
        UUID postId = UUID.randomUUID();
        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.postToPostResponseDTO(post)).thenReturn(
                PostResponseDTO.builder()
                        .title("Test Title")
                        .content("Test Content")
                        .build()
        );
        PostResponseDTO responseDTOS = postService.findById(postId.toString());

        assertEquals("Test Title", responseDTOS.getTitle());
    }

    @Test
    public void test_findById_postNotFound() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.findById(UUID.randomUUID().toString())
        );
    }

    @Test
    public void test_deactivatePost_withSuccess() throws AccessDeniedException {

        UUID postId = UUID.randomUUID();
        Author author = mock(Author.class);
        Post post = mock(Post.class);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(post.getAuthor()).thenReturn(author);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.postToPostResponseDTO(post)).thenReturn(PostResponseDTO.builder()
                .id(UUID.randomUUID())
                .title("Test Title")
                .build());

        PostResponseDTO responseDTO = postService.deactivatePost(postId.toString());

        assertEquals("Test Title", responseDTO.getTitle());
    }

    @Test
    public void test_activatePost_withSuccess() {

        UUID postId = UUID.randomUUID();
        Author author = mock(Author.class);
        Post post = mock(Post.class);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.postToPostResponseDTO(post)).thenReturn(PostResponseDTO.builder()
                .id(UUID.randomUUID())
                .title("Test Title")
                .build());

        PostResponseDTO responseDTO = postService.activatePost(postId.toString());

        assertEquals("Test Title", responseDTO.getTitle());
    }

    @Test
    public void test_updatePost_withSuccess() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostUpdateRequestDTO requestDTO =
                new PostUpdateRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile, true);

        Category category = mock(Category.class);
        Post post = mock(Post.class);
        UUID postId = UUID.randomUUID();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(categoryRepository.findByName("Siyaset")).thenReturn(Optional.of(category));

        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.postToPostResponseDTO(post)).thenReturn(PostResponseDTO.builder()
                .id(postId)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .build());

        PostResponseDTO responseDTO = postService.updatePost(postId.toString(), requestDTO);

        assertEquals(requestDTO.getTitle(), responseDTO.getTitle());


    }

    @Test
    public void test_updatePost_postNotFound() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostUpdateRequestDTO requestDTO =
                new PostUpdateRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile, true);

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.updatePost(UUID.randomUUID().toString(), requestDTO)
        );


    }

    @Test
    public void test_updatePost_categoryNotFound() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostUpdateRequestDTO requestDTO =
                new PostUpdateRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile, true);

        Post post = mock(Post.class);
        UUID postId = UUID.randomUUID();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.updatePost(postId.toString(), requestDTO)
        );

    }

    @Test
    public void test_updatePostEditor_withSuccess() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostUpdateEditorRequestDTO requestDTO =
                new PostUpdateEditorRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile, true);

        Category category = mock(Category.class);
        Post post = mock(Post.class);
        UUID postId = UUID.randomUUID();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(categoryRepository.findByName("Siyaset")).thenReturn(Optional.of(category));

        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.postToPostResponseDTO(post)).thenReturn(PostResponseDTO.builder()
                .id(postId)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .build());

        PostResponseDTO responseDTO = postService.updatePostEditor(postId.toString(), requestDTO);

        assertEquals(requestDTO.getTitle(), responseDTO.getTitle());


    }

    @Test
    public void test_updatePostEditor_postNotFound() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostUpdateEditorRequestDTO requestDTO =
                new PostUpdateEditorRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile, true);

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.updatePostEditor(UUID.randomUUID().toString(), requestDTO)
        );


    }

    @Test
    public void test_updatePostEditor_categoryNotFound() throws IOException {

        String imagePath = new ClassPathResource("wall1677252684787.jpg").getPath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/test/resources/" + imagePath);
        MultipartFile multipartFile = new MockMultipartFile("wall1677252684787.jpg",
                "wall1677252684787.jpg", "image/jpeg", Files.readAllBytes(path));

        PostUpdateEditorRequestDTO requestDTO =
                new PostUpdateEditorRequestDTO("Test Title", null, "Test Content", "Siyaset", multipartFile, true);

        Post post = mock(Post.class);
        UUID postId = UUID.randomUUID();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.updatePostEditor(postId.toString(), requestDTO)
        );

    }

    @Test
    public void test_getRandomThreePost_withSuccess() {
        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(postRepository.findThreeActiveTrueByRandom()).thenReturn(List.of(post));

        List<PostResponseDTO> responseDTOS = postService.getRandomThreePost();

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_getPostsByCategory_withSuccess() {
        Category category = mock(Category.class);

        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(categoryRepository.findByName("Siyaset")).thenReturn(Optional.of(category));
        when(postRepository.findAllByCategoryNameAndActiveTrueOrderByCreateDateTimeDesc("Siyaset"))
                .thenReturn(List.of(post));

        List<PostResponseDTO> responseDTOS = postService.getPostsByCategory("Siyaset");

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_getPostsByCategory_categoryNotFound() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.getPostsByCategory("wrongCategory")
        );
    }

    @Test
    public void test_findByUsername_withSuccess() {
        Author author = mock(Author.class);

        Post post = Post.Builder.newBuilder()
                .postId(UUID.randomUUID())
                .title("Test Title")
                .content("Test Content")
                .build();

        when(authorRepository.findByUsername("ensar")).thenReturn(Optional.of(author));
        when(postRepository.findAllByAuthorUsernameAndActiveTrueOrderByCreateDateTimeDesc("ensar"))
                .thenReturn(List.of(post));

        List<PostResponseDTO> responseDTOS = postService.findByUsername("ensar");

        assertEquals(1, responseDTOS.size());
    }

    @Test
    public void test_findByUsername_categoryNotFound() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.findByUsername("wrongUsername")
        );
    }

    @Test
    public void test_countsByCategoryName_withSuccess() {
        Category category = mock(Category.class);

        when(categoryRepository.findByName("Siyaset")).thenReturn(Optional.of(category));
        when(postRepository.countByCategoryNameAndActiveTrue("Siyaset")).thenReturn(2);

        Integer postCount = postService.countsByCategoryName("Siyaset");

        assertEquals(2, postCount);

    }

    @Test
    public void test_countsByCategoryName_categoryNotFound() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> postService.countsByCategoryName("wrongCategory")
        );
    }

}