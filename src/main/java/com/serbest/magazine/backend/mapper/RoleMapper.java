package com.serbest.magazine.backend.mapper;


import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponseDTO roleToRoleResponseDTO(Role role){
        return new RoleResponseDTO(role.getName());
    }
}
