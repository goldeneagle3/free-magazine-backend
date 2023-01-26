package com.serbest.magazine.backend.service.impl;

import com.google.common.base.Strings;
import com.serbest.magazine.backend.dto.auth.*;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.RefreshToken;
import com.serbest.magazine.backend.entity.Role;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.TokenRefreshException;
import com.serbest.magazine.backend.mapper.UserMapper;
import com.serbest.magazine.backend.repository.AuthorRepository;
import com.serbest.magazine.backend.repository.RoleRepository;
import com.serbest.magazine.backend.security.jwt.JwtUtils;
import com.serbest.magazine.backend.security.services.RefreshTokenService;
import com.serbest.magazine.backend.security.services.UserDetailsImpl;
import com.serbest.magazine.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthorRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(AuthorRepository userRepository, AuthenticationManager authenticationManager,
                           RoleRepository roleRepository, UserMapper userMapper, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO requestDTO) {

        // add check for username exists in database
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Username is already exists!");
        }

        // add check for email exists in database
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Email is already exists!");
        }

        List<Author> authors = userRepository.findAll().stream().collect(Collectors.toList());

        if (authors.size() == 0){
            Author user = userMapper.registerRequestDTOToUser(requestDTO);
            Set<Role> roles = new HashSet<>();

            Role newAdminRole = roleRepository.save(new Role("ROLE_ADMIN"));
            Role newEditorRole = roleRepository.save(new Role("ROLE_EDITOR"));
            Role newAuthorRole = roleRepository.save(new Role("ROLE_AUTHOR"));
            Role newUserRole = roleRepository.save(new Role("ROLE_USER"));

            roles.add(newAdminRole);
            roles.add(newEditorRole);
            roles.add(newAuthorRole);
            roles.add(newUserRole);
            user.setRoles(roles);

            Author newUser = userRepository.save(user);
            return new RegisterResponseDTO(newUser.getUsername());

        }

        Author user = userMapper.registerRequestDTOToUser(requestDTO);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        Author newUser = userRepository.save(user);

        return new RegisterResponseDTO(newUser.getUsername());
    }

    @Override
    public JWTAuthResponse login(HttpServletRequest request, LoginRequestDTO loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(accessToken);
        jwtAuthResponse.setRefreshTokenCookie(jwtRefreshCookie);
        jwtAuthResponse.setRoles(roles);
        jwtAuthResponse.setUserId(userDetails.getId());
        jwtAuthResponse.setEmail(userDetails.getEmail());
        jwtAuthResponse.setUsername(userDetails.getUsername());
        jwtAuthResponse.setImageId(userDetails.getImageId());

        return jwtAuthResponse;
    }

    @Override
    public RefreshTokenResponseDTO refreshTokenHandle(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (refreshToken.length() > 0)) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getAuthor)
                    .map(author -> {
                        String accessToken = jwtUtils.generateAccessToken(author);
                        RefreshTokenResponseDTO refreshTokenResponseDTO = userMapper.authorToRefreshTokenResponseDTO(author);
                        refreshTokenResponseDTO.setAccessToken(accessToken);
                        return refreshTokenResponseDTO;
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return new RefreshTokenResponseDTO(null, null, null, null, "Refresh Token is empty!");
    }

    @Override
    public ResponseCookie logout() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principle.toString() != "anonymousUser") {
            String username = ((UserDetailsImpl) principle).getUsername();
            refreshTokenService.deleteByUsername(username);
        }

        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
        return jwtRefreshCookie;
    }

}
