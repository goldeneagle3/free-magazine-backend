package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.auth.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.io.IOException;

public interface AuthService {

    RegisterResponseDTO register(RegisterRequestDTO requestDTO);
    JWTAuthResponse login(HttpServletRequest request, LoginRequestDTO loginDto);
    RefreshTokenResponseDTO refreshTokenHandle(HttpServletRequest request);
    ResponseCookie logout();
}
