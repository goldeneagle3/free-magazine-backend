package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;

import java.util.List;

public interface RoleService {
    MessageResponseDTO createRole(RoleRequestDTO requestDTO);
    MessageResponseDTO updateRole(String roleName , RoleRequestDTO requestDTO);
    List<RoleResponseDTO> getRoles();
}
