package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.entity.Role;
import com.serbest.magazine.backend.service.RoleService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private ResponseEntity<String> createRole(@RequestBody RoleRequestDTO requestDTO){
        return ResponseEntity.ok(roleService.createRole(requestDTO));
    }

    @PatchMapping("/{roleName}")
    private ResponseEntity<String> updateRole(@PathVariable String roleName,@RequestBody RoleRequestDTO requestDTO){
        return ResponseEntity.ok(roleService.updateRole(roleName,requestDTO));
    }

    @GetMapping
    private ResponseEntity<List<RoleResponseDTO>> getRoles(){
        return ResponseEntity.ok(roleService.getRoles());
    }
}
