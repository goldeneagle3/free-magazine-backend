package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;

import java.util.List;

public interface RoleService {
    String createRole(RoleRequestDTO requestDTO);
    String updateRole(String roleName , RoleRequestDTO requestDTO);
    List<RoleResponseDTO> getRoles();
}
