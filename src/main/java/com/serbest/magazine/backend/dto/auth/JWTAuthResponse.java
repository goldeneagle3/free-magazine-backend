package com.serbest.magazine.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
    private String accessToken;
    private ResponseCookie refreshTokenCookie;
    private String message;
    private UUID userId;
    private String username;
    private String email;
    private String image;
    private List<String> roles;
}
