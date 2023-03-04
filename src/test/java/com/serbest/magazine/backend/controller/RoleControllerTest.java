package com.serbest.magazine.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serbest.magazine.backend.dto.auth.RoleRequestDTO;
import com.serbest.magazine.backend.dto.auth.RoleResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.service.RoleService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_createRole_whenInputValid_thenReturn200() throws Exception {

        // given
        RoleRequestDTO requestDTO = new RoleRequestDTO(generateRandomCategoryName());

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/roles")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(requestDTO)));

        // then
        ArgumentCaptor<RoleRequestDTO> captor = ArgumentCaptor.forClass(RoleRequestDTO.class);
        verify(roleService, times(1)).createRole(captor.capture());
        assertEquals(captor.getValue().getName(), requestDTO.getName());
        actions.andExpect(status().isOk());

    }

    @Test
    public void test_createRole_whenNotAuthenticated_thenReturn401() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/roles")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("test")));

        actions.andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "AUTHOR", "EDITOR"})
    public void test_createRole_whenNotAuthorized_thenReturn403() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/roles")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("test")));

        actions.andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_createRole_whenInputNotValid_thenReturn400() throws Exception {

        // given
        RoleRequestDTO requestDTO = new RoleRequestDTO("SiyasetSiyasetSiyasetSiyaset");

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/roles")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(requestDTO)));

        // then
        actions.andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_createRole_whenInputNotProvided_thenReturn400() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/categories")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("")));

        // then
        actions.andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateRole_whenInputValid_thenReturn200() throws Exception {
        UUID randomId = UUID.randomUUID();
        // given
        RoleRequestDTO requestDTO = new RoleRequestDTO(generateRandomCategoryName());
        MessageResponseDTO resultExpected = new MessageResponseDTO("Role with id : " + randomId + " is updated with name : "
                + requestDTO.getName() + ".");
        when(roleService.updateRole(requestDTO.getName(), requestDTO)).thenReturn(resultExpected);

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/administration/roles/" + requestDTO.getName())
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(roleService,times(1)).updateRole(requestDTO.getName(),requestDTO);
        assertThat(objectMapper.writeValueAsString(resultExpected))
                .isEqualToIgnoringWhitespace(responseBody);

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateRole_whenInputValid_thenReturn200_differentStyle() throws Exception {
        UUID randomId = UUID.randomUUID();

        // given
        RoleRequestDTO requestDTO = new RoleRequestDTO("ROLE_EDITOR");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/roles/" + randomId)
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void test_updateRole_whenNotAuthenticated_thenReturn401() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(put("/api/administration/roles/kdawdawh")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("test")));

        actions.andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "AUTHOR", "EDITOR"})
    public void test_updateRole_whenNotAuthorized_thenReturn403() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(put("/api/administration/roles/jdadawjk")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("test")));

        actions.andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateRole_whenInputNotValid_thenReturn400() throws Exception {

        // given
        RoleRequestDTO requestDTO = new RoleRequestDTO("SiyasetSiyasetSiyasetSiyaset");

        // when
        ResultActions actions = mockMvc.perform(put("/api/administration/roles/ROLE_OLD")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(requestDTO)));

        // then
        actions.andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateRole_whenInputNotProvided_thenReturn400() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(put("/api/administration/roles/ROLE_OLD")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("")));

        // then
        actions.andExpect(status().isBadRequest());

    }

    @Test
    public void test_getRoles_whenRolesFound_thenReturn200AndRoles() throws Exception {

        // given
        RoleResponseDTO responseDTO = new RoleResponseDTO( "ROLE_EDITOR");
        RoleResponseDTO responseDTO2 = new RoleResponseDTO("ROLE_AUTHOR");
        when(roleService.getRoles()).thenReturn(Arrays.asList(responseDTO, responseDTO2));

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/administration/roles")
                .accept(CONTENT_TYPE)).andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(roleService, times(1)).getRoles();
        assertThat(objectMapper.writeValueAsString(Arrays.asList(responseDTO, responseDTO2)))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    public void test_getRoles_whenNoRoleFound_thenReturn200AndEmptyList() throws Exception {

        // given
        when(roleService.getRoles()).thenReturn(Collections.emptyList());

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/administration/roles")
                .accept(CONTENT_TYPE)).andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(roleService, times(1)).getRoles();
        assertThat(objectMapper.writeValueAsString(Collections.emptyList()))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    public String generateRandomCategoryName() {

        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        return generatedString;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}