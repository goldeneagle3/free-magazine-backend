package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.author.AuthorCardResponseDTO;
import com.serbest.magazine.backend.dto.author.AuthorListResponseDTO;
import com.serbest.magazine.backend.dto.user.AuthorResponseDTO;
import com.serbest.magazine.backend.dto.user.AuthorUpdateRequestDTO;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Role;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.UserMapper;
import com.serbest.magazine.backend.repository.AuthorRepository;
import com.serbest.magazine.backend.repository.RoleRepository;
import com.serbest.magazine.backend.security.CheckAuthorization;
import com.serbest.magazine.backend.service.AuthorService;
import com.serbest.magazine.backend.util.UploadImage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final RoleRepository roleRepository;
    private final AuthorRepository authorRepository;
    private final UserMapper userMapper;
    private final CheckAuthorization checkAuthorization;

    public AuthorServiceImpl(RoleRepository roleRepository, AuthorRepository authorRepository, UserMapper userMapper,
                             CheckAuthorization checkAuthorization) {
        this.roleRepository = roleRepository;
        this.authorRepository = authorRepository;
        this.userMapper = userMapper;
        this.checkAuthorization = checkAuthorization;
    }

    @Override
    public List<AuthorResponseDTO> getUsers() {
        List<Role> roles = new ArrayList<>();
        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        roles.add(role.get());
        List<Author> authors = authorRepository.findByRolesIn(roles);
        return authors.stream().map(
                userMapper::authorToAuthorResponseDTO
        ).collect(Collectors.toList());
    }

    @Override
    public List<AuthorListResponseDTO> getAuthors() {
        List<Role> roles = new ArrayList<>();
        Optional<Role> role = roleRepository.findByName("ROLE_AUTHOR");
        roles.add(role.get());
        List<Author> authors = authorRepository.findByRolesIn(roles);
        return authors.stream().map(
                author -> new AuthorListResponseDTO(author.getId(), author.getUsername())
        ).collect(Collectors.toList());
    }

    @Override
    public List<AuthorCardResponseDTO> getAuthorsForCard() {
        List<Role> roles = new ArrayList<>();
        Optional<Role> role = roleRepository.findByName("ROLE_AUTHOR");
        roles.add(role.get());
        List<Author> authors = authorRepository.findByRolesIn(roles);
        return authors.stream().map(userMapper::authorToAuthorCardResponseDTO).collect(Collectors.toList());
    }

    @Override
    public AuthorResponseDTO updateUser(String userId, AuthorUpdateRequestDTO requestDTO) throws IOException {
        Author author = getAuthor(userId);

        author.setFirstName(requestDTO.getFirstName());
        author.setLastName(requestDTO.getLastName());
        author.setDescription(requestDTO.getDescription());
        if (!requestDTO.getImageProtect()) {
            author.setProfileImage(UploadImage.uploadImage(requestDTO.getImage()));
        }

        return userMapper.authorToAuthorResponseDTO(authorRepository.save(author));
    }

    @Override
    public AuthorResponseDTO makeAuthor(String userId) {
        Author author = authorRepository.findById(UUID.fromString(userId)).orElseThrow(
                () -> new ResourceNotFoundException("Author", "id", userId)
        );

        Role authorRole = roleRepository.findByName("ROLE_AUTHOR").orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", "ROLE_AUTHOR")
        );

        Set<Role> userRoles = author.getRoles();
        userRoles.add(authorRole);

        author.setRoles(userRoles);

        return userMapper.authorToAuthorResponseDTO(authorRepository.save(author));
    }

    @Override
    public AuthorResponseDTO makeEditor(String userId) {
        Author author = authorRepository.findById(UUID.fromString(userId)).orElseThrow(
                () -> new ResourceNotFoundException("Author", "id", userId)
        );

        Role editorRole = roleRepository.findByName("ROLE_EDITOR").orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", "ROLE_EDITOR")
        );

        Set<Role> userRoles = author.getRoles();

        userRoles.add(editorRole);

        author.setRoles(userRoles);

        return userMapper.authorToAuthorResponseDTO(authorRepository.save(author));
    }

    @Override
    public AuthorResponseDTO getAuthorByUsername(String username) {

        Role authorRole = roleRepository.findByName("ROLE_AUTHOR").orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", "ROLE_AUTHOR")
        );

        Author author = authorRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Author", "username", username)
        );

        if (!author.getRoles().contains(authorRole)) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Sadece yazarların profil sayfası olabilir.");
        }
        return userMapper.authorToAuthorResponseDTO(author);
    }

    @Override
    public AuthorResponseDTO deactivateUser(String userId) throws AccessDeniedException {
        Author author = getAuthor(userId);
        author.setActive(false);
        return userMapper.authorToAuthorResponseDTO(authorRepository.save(author));
    }

    @Override
    public void deleteCompleteUser(String username) {
        Author author = authorRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Author", "username", username)
        );
        author.setRoles(new HashSet<>());
        authorRepository.delete(author);
    }

    private Author getAuthor(String userId) throws AccessDeniedException {

        Author author = authorRepository.findById(UUID.fromString(userId)).orElseThrow(
                () -> new ResourceNotFoundException("Author", "id", userId)
        );
        checkAuthorization.checkUser(author);
        return author;
    }
}
