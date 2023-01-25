package com.serbest.magazine.backend.service;


import com.serbest.magazine.backend.dto.author.AuthorCardResponseDTO;
import com.serbest.magazine.backend.dto.author.AuthorListResponseDTO;
import com.serbest.magazine.backend.dto.user.AuthorResponseDTO;
import com.serbest.magazine.backend.dto.user.AuthorUpdateRequestDTO;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

public interface AuthorService {

    List<AuthorResponseDTO> getUsers();
    List<AuthorListResponseDTO> getAuthors();
    AuthorResponseDTO updateUser(String userId, AuthorUpdateRequestDTO requestDTO) throws IOException;
    AuthorResponseDTO makeAuthor(String userId);
    AuthorResponseDTO makeEditor(String userId);
    AuthorResponseDTO getAuthorByUsername(String userId);
    List<AuthorCardResponseDTO> getAuthorsForCard();
    AuthorResponseDTO deactivateUser(String userId) throws AccessDeniedException;
    void deleteCompleteUser(String username);
}
