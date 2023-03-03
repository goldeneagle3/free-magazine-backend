package com.serbest.magazine.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.service.CategoryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;


    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_createCategory_whenInputValid_thenReturn200() throws Exception {

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Siyaset");

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/categories")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(requestDTO)));

        // then
        ArgumentCaptor<CategoryRequestDTO> captor = ArgumentCaptor.forClass(CategoryRequestDTO.class);
        verify(categoryService, times(1)).createCategory(captor.capture());
        assertEquals(captor.getValue().getName(), "Siyaset");
        actions.andExpect(status().isOk());

    }

    @Test
    public void test_createCategory_whenNotAuthenticated_thenReturn401() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/categories")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("test")));

        actions.andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "AUTHOR", "EDITOR"})
    public void test_createCategory_whenNotAuthorized_thenReturn403() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/categories")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("test")));

        actions.andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_createCategory_whenInputNotValid_thenReturn400() throws Exception {

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("SiyasetSiyasetSiyasetSiyaset");

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/categories")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(requestDTO)));

        // then
        actions.andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_createCategory_whenInputNotProvided_thenReturn400() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(post("/api/administration/categories")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString("")));

        // then
        actions.andExpect(status().isBadRequest());

    }

    @Test
    public void test_findAllCategories_whenCategoriesFound_thenReturn200AndCategories() throws Exception {

        // given
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(UUID.randomUUID(), "Siyaset", 2);
        CategoryResponseDTO responseDTO2 = new CategoryResponseDTO(UUID.randomUUID(), "Hukuk", 2);
        when(categoryService.getAllCategory()).thenReturn(Arrays.asList(responseDTO, responseDTO2));

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/administration/categories")
                .accept(CONTENT_TYPE)).andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(categoryService, times(1)).getAllCategory();
        assertThat(objectMapper.writeValueAsString(Arrays.asList(responseDTO, responseDTO2)))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    public void test_findAllCategories_whenNoCategoryFound_thenReturn200AndEmptyList() throws Exception {

        // given
        when(categoryService.getAllCategory()).thenReturn(Collections.emptyList());

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/administration/categories")
                .accept(CONTENT_TYPE)).andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(categoryService, times(1)).getAllCategory();
        assertThat(objectMapper.writeValueAsString(Collections.emptyList()))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateCategory_whenInputValid_thenReturn200() throws Exception {

        UUID randomId = UUID.randomUUID();

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Siyaset");
        MessageResponseDTO resultExpected = new MessageResponseDTO("Category with id : " + randomId
                + " updated with new name : " + requestDTO.getName());
        when(categoryService.updateCategory(randomId.toString(), requestDTO)).thenReturn(resultExpected);

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/administration/categories/" + randomId)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(categoryService, times(1)).updateCategory(randomId.toString(), requestDTO);
        assertThat(objectMapper.writeValueAsString(resultExpected))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateCategory_whenInputValid_thenReturn200_differentStyle() throws Exception {
        UUID randomId = UUID.randomUUID();

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Siyaset");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/" + randomId)
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateCategory_whenInputNotValid_thenReturn400() throws Exception {
        UUID randomId = UUID.randomUUID();

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("SiyasetSiyasetSiyasetSiyasetSiyaset");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/" + randomId)
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_updateCategory_whenInputNotProvided_thenReturn400() throws Exception {
        UUID randomId = UUID.randomUUID();

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/" + randomId)
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    public void test_updateCategory_whenNotAuthenticated_thenReturn401() throws Exception {
        UUID randomId = UUID.randomUUID();

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/" + randomId)
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "AUTHOR", "EDITOR"})
    public void test_updateCategory_whenNotAuthorized_thenReturn403() throws Exception {
        UUID randomId = UUID.randomUUID();

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/" + randomId)
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_deleteCategoryById_whenInputValid_thenReturn200() throws Exception {

        UUID randomId = UUID.randomUUID();

        // given
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Siyaset");
        MessageResponseDTO resultExpected = new MessageResponseDTO("Category with id : " + randomId + " is deleted.");
        when(categoryService.deleteCategory(randomId.toString())).thenReturn(resultExpected);

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/administration/categories/deleteCategory/" + randomId)
                        .contentType(CONTENT_TYPE))
                .andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(categoryService, times(1)).deleteCategory(randomId.toString());
        assertThat(objectMapper.writeValueAsString(resultExpected))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_deleteCategoryById_whenInputValid_thenReturn200_differentStyle() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/deleteCategory/" + randomId))
                .andExpect(status().isOk());
    }

    @Test
    public void test_deleteCategoryById_whenNotAuthenticated_thenReturn401() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/deleteCategory/" + randomId))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "AUTHOR", "EDITOR"})
    public void test_deleteCategoryById_whenNotAuthorized_thenReturn403() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/administration/categories/deleteCategory/" + randomId))
                .andExpect(status().is(403));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}