package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.auth.*;
import com.serbest.magazine.backend.mapper.UserMapper;
import com.serbest.magazine.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:3000", "https://magazine-app.netlify.app"}, maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }


    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<UserInfoResponse> login(@RequestBody LoginRequestDTO loginDto, HttpServletRequest request) {
        JWTAuthResponse jwtAuthResponse = authService.login(request, loginDto);
        System.out.println(jwtAuthResponse.getRefreshTokenCookie());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtAuthResponse.getRefreshTokenCookie().toString())
                .body(userMapper.jwtAuthResponseToUserInfoResponse(jwtAuthResponse));
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerDto){
        RegisterResponseDTO response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshTokenHandle(request));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> logoutUser() {
        ResponseCookie jwtRefreshCookie = authService.logout();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("You've been signed out!");
    }
}
