package com.serbest.magazine.backend.mapper;

import com.serbest.magazine.backend.dto.auth.JWTAuthResponse;
import com.serbest.magazine.backend.dto.auth.RefreshTokenResponseDTO;
import com.serbest.magazine.backend.dto.auth.RegisterRequestDTO;
import com.serbest.magazine.backend.dto.auth.UserInfoResponse;
import com.serbest.magazine.backend.dto.author.AuthorCardResponseDTO;
import com.serbest.magazine.backend.dto.user.AuthorResponseDTO;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.ImageModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Author registerRequestDTOToUser(RegisterRequestDTO requestDTO) {
        return Author.Builder
                .newBuilder()
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .profileImage(new ImageModel())
                .active(true)
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .build();
    }

    public AuthorResponseDTO authorToAuthorResponseDTO(Author author) {
        return AuthorResponseDTO.builder()
                .id(author.getId())
                .username(author.getUsername())
                .email(author.getEmail())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .description(author.getDescription())
                .imageId(author.getProfileImage().getId())
                .createDateTime(author.getCreateDateTime())
                .updateDateTime(author.getUpdateDateTime())
                .build();
    }

    public AuthorCardResponseDTO authorToAuthorCardResponseDTO(Author author) {
        return AuthorCardResponseDTO.builder()
                .id(author.getId())
                .username(author.getUsername())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .description(author.getDescription())
                .imageId(author.getProfileImage().getId())
                .imageName(author.getProfileImage().getName())
                .build();
    }

    public UserInfoResponse jwtAuthResponseToUserInfoResponse(JWTAuthResponse jwtAuthResponse) {
        return new UserInfoResponse(
                jwtAuthResponse.getUserId(),
                jwtAuthResponse.getUsername(),
                jwtAuthResponse.getEmail(),
                jwtAuthResponse.getImageId(),
                jwtAuthResponse.getRoles(),
                jwtAuthResponse.getAccessToken()
        );
    }

    public RefreshTokenResponseDTO authorToRefreshTokenResponseDTO(Author author) {
        return RefreshTokenResponseDTO.builder()
                .username(author.getUsername())
                .imageId(author.getProfileImage().getId())
                .roles(author.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()))
                .build();
    }

}
