package com.serbest.magazine.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private UUID userId;
    private String username;
    private String email;
    private String image;
    private List<String> roles;
    private String accessToken;
}
