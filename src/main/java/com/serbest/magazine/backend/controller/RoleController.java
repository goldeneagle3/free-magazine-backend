package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.service.RoleService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administration/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    private ResponseEntity<MessageResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO requestDTO) {
        return ResponseEntity.ok(roleService.createRole(requestDTO));
    }

    @PutMapping("/{roleName}")
    private ResponseEntity<MessageResponseDTO> updateRole(@PathVariable String roleName, @Valid @RequestBody RoleRequestDTO requestDTO) {
        return ResponseEntity.ok(roleService.updateRole(roleName, requestDTO));
    }

    @GetMapping
    private ResponseEntity<List<RoleResponseDTO>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }
}
