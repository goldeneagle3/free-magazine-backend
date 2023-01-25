package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.entity.Role;
import com.serbest.magazine.backend.mapper.RoleMapper;
import com.serbest.magazine.backend.repository.RoleRepository;
import com.serbest.magazine.backend.service.RoleService;
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
    public String createRole(RoleRequestDTO requestDTO) {
        Role role = roleRepository.save(new Role(requestDTO.getName()));

        return "New Role named : " + role.getName() + " is created.";
    }

    @Override
    public String updateRole(String roleName , RoleRequestDTO requestDTO) {
        Optional<Role> role = roleRepository.findByName(roleName);

        if (role.isEmpty()){
            return "Role is not found!";
        }

        role.get().setName(requestDTO.getName());

        Role updatedRole = roleRepository.save(role.get());
        return "Role with id : " + updatedRole.getId().toString() + " is updated with name : "
                + updatedRole.getName() + ".";
    }

    @Override
    public List<RoleResponseDTO> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleMapper::roleToRoleResponseDTO)
                .collect(Collectors.toList());
    }
}
