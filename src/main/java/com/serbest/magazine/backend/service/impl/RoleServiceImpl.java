package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Role;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.RoleMapper;
import com.serbest.magazine.backend.repository.RoleRepository;
import com.serbest.magazine.backend.service.RoleService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public MessageResponseDTO createRole(RoleRequestDTO requestDTO) {
        validateAndSanitizeRoleName(requestDTO.getName());

        try {
            Role role = roleRepository.save(new Role(requestDTO.getName()));
            return new MessageResponseDTO("New Role named : " + role.getName() + " is created.");
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public MessageResponseDTO updateRole(String roleName, RoleRequestDTO requestDTO) {
        validateAndSanitizeRoleName(roleName);
        validateAndSanitizeRoleName(requestDTO.getName());

        Optional<Role> role = roleRepository.findByName(roleName);

        if (!role.isPresent()) {
            throw new ResourceNotFoundException("Role", "name", roleName);
        }

        role.get().setName(requestDTO.getName());

        try {
            Role updatedRole = roleRepository.save(role.get());
            return new MessageResponseDTO("Role with id : " + updatedRole.getId() + " is updated with name : "
                    + updatedRole.getName() + ".");
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<RoleResponseDTO> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleMapper::roleToRoleResponseDTO)
                .collect(Collectors.toList());
    }

    private void validateAndSanitizeRoleName(String roleName) {
        Assert.notNull(roleName);
        if (!roleName.startsWith("ROLE_")) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Provide a valid role name , please.");
        }
    }
}
