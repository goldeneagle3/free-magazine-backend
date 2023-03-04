package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Role;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.repository.RoleRepository;
import com.serbest.magazine.backend.service.RoleService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class RoleServiceImplIntegrationTest {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testIntegration_createRole_success() {
        RoleRequestDTO requestDTO = new RoleRequestDTO(generateRandomRoleName());

        MessageResponseDTO responseDTO = roleService.createRole(requestDTO);
        Optional<Role> role = roleRepository.findByName(requestDTO.getName());

        assertEquals(role.get().getName(), requestDTO.getName());
        assertEquals(responseDTO.getMessage(), "New Role named : " + role.get().getName() + " is created.");
    }

    @Test
    public void testIntegration_createRole_inCorrectRoleName() {
        RoleRequestDTO requestDTO = new RoleRequestDTO("Wrong_Test_Name");

        assertThrowsExactly(CustomApplicationException.class, () -> roleService.createRole(requestDTO));
    }

    @Test
    public void testIntegration_updateRole_success() {
        RoleRequestDTO requestDTO = new RoleRequestDTO(generateRandomRoleName());
        RoleRequestDTO requestUpdateDTO = new RoleRequestDTO(generateRandomRoleName());

        Role role = roleRepository.save(new Role(requestDTO.getName()));
        MessageResponseDTO responseDTO = roleService.updateRole(role.getName(), requestUpdateDTO);

        assertEquals(
                responseDTO.getMessage(),
                "Role with id : " + role.getId() + " is updated with name : "
                        + requestUpdateDTO.getName() + ".");
    }

    @Test
    public void testIntegration_updateRole_nameNotProvided() {
        RoleRequestDTO requestDTO = new RoleRequestDTO(generateRandomRoleName());
        RoleRequestDTO requestUpdateDTO = new RoleRequestDTO(generateRandomRoleName());

        roleRepository.save(new Role(requestDTO.getName()));
        assertThrows(
                IllegalArgumentException.class,
                () -> roleService.updateRole(null, requestUpdateDTO)
        );
    }

    @Test
    public void testIntegration_updateRole_withIncorrectName() {
        RoleRequestDTO requestDTO = new RoleRequestDTO("Wrong_Role_Name");
        RoleRequestDTO requestUpdateDTO = new RoleRequestDTO(generateRandomRoleName());

        roleRepository.save(new Role(requestDTO.getName()));
        assertThrows(
                CustomApplicationException.class,
                () -> roleService.updateRole(requestDTO.getName(), requestUpdateDTO)
        );
    }

    @Test
    public void testIntegration_updateRole_roleNotFound() {
        RoleRequestDTO requestDTO = new RoleRequestDTO(generateRandomRoleName());
        RoleRequestDTO requestUpdateDTO = new RoleRequestDTO(generateRandomRoleName());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roleService.updateRole(requestDTO.getName(), requestUpdateDTO)
        );
    }

    @Test
    public void testIntegration_getRoles_success(){
        RoleRequestDTO requestDTO = new RoleRequestDTO(generateRandomRoleName());

        roleRepository.save(new Role(requestDTO.getName()));
        List<RoleResponseDTO> roles = roleService.getRoles();

        assertEquals(roles.size(), 1);
    }

    private String generateRandomRoleName() {

        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        return "ROLE_" + generatedString;
    }

}