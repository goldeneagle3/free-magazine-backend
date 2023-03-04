package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Role;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.mapper.RoleMapper;
import com.serbest.magazine.backend.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @InjectMocks
    RoleServiceImpl roleService;

    @Mock
    RoleRepository roleRepository;

    @Mock
    RoleMapper roleMapper;

    @Test
    public void test_createRole_withSuccess(){
        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_TEST");

        Role roleMock = Mockito.mock(Role.class);
        UUID randomId = UUID.randomUUID();

        when(roleMock.getId()).thenReturn(randomId);
        when(roleMock.getName()).thenReturn("ROLE_TEST");
        when(roleRepository.save(any(Role.class))).thenReturn(roleMock);
        Role role = roleRepository.save(new Role(requestDTO.getName()));

        assertEquals(role.getName(), requestDTO.getName());
        assertEquals(role.getId(), randomId);
    }

    @Test
    public void test_createRole_checkReturn() {
        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_TEST");

        Role roleMock = Mockito.mock(Role.class);

        when(roleMock.getName()).thenReturn("ROLE_TEST");
        when(roleRepository.save(any(Role.class))).thenReturn(roleMock);
        MessageResponseDTO result = roleService.createRole(requestDTO);

        assertEquals(result.getMessage(), "New Role named : " + requestDTO.getName() + " is created.");
    }

    @Test
    public void test_createRole_withMissingArg() {
        RoleRequestDTO requestDTO = new RoleRequestDTO();

        assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(requestDTO)
        );
    }

    @Test
    public void test_createRole_withInCorrectArg() {
        RoleRequestDTO requestDTO = new RoleRequestDTO("RoleThatNotStartWithROLE_");

        assertThrows(
                CustomApplicationException.class,
                () -> roleService.createRole(requestDTO)
        );
    }

    @Test
    public void test_updateRole_withSuccess() {
        UUID randomId = UUID.randomUUID();
        Role roleMock = new Role(randomId, "ROLE_TEST");

        when(roleRepository.findByName(roleMock.getName())).thenReturn(Optional.of(roleMock));

        roleMock.setName("ROLE_UPDATED_TEST");
        when(roleRepository.save(any(Role.class))).thenReturn(roleMock);
        MessageResponseDTO messageResponseDTO =
                roleService.updateRole("ROLE_TEST", new RoleRequestDTO("ROLE_UPDATED_TEST"));

        assertEquals(messageResponseDTO.getMessage(),"Role with id : "
                + randomId
                + " is updated with name : ROLE_UPDATED_TEST.");
    }

    @Test
    public void test_updateRole_withError(){

        // No Save Instruction.

        assertThrows(
                CustomApplicationException.class,
                () -> roleService.updateRole("TEST_USER", new RoleRequestDTO("ROLE_TEST"))
        );
    }

    @Test
    public void test_updateRole_withoutProvidedName(){
        assertThrows(
                IllegalArgumentException.class,
                () -> roleService.updateRole(null,new RoleRequestDTO("ROLE_TEST"))
        );
    }

    @Test
    public void test_getRoles_withSuccess() {
        Role role1 = new Role(UUID.randomUUID(), "ROLE_ADMIN");
        Role role2 = new Role(UUID.randomUUID(), "ROLE_EDITOR");
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));
        List<RoleResponseDTO> roleResponseDTOS = roleService.getRoles();

        assertEquals(2, roleResponseDTOS.size());
    }

}