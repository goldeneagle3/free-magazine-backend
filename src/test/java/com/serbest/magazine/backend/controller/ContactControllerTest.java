package com.serbest.magazine.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactListResponseDTO;
import com.serbest.magazine.backend.dto.contact.ContactRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactResponseDTO;
import com.serbest.magazine.backend.service.ContactService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactService contactService;

    @Test
    public void test_contactUsSendMessage_withValidInput_thenReturn200() throws Exception {
        // given
        ContactRequestDTO requestDTO = new ContactRequestDTO("test@email.com", "titlist", "conentis");

        // when
        ResultActions actions = mockMvc.perform(post("/api/contact")
                .contentType(CONTENT_TYPE)
                .content(objectMapper.writeValueAsString(requestDTO)));

        // then
        ArgumentCaptor<ContactRequestDTO> captor = ArgumentCaptor.forClass(ContactRequestDTO.class);
        verify(contactService, times(1)).sendMessage(captor.capture());
        assertEquals(captor.getValue().getTitle(), "titlist");
        actions.andExpect(status().isCreated());
    }

    @Test
    public void test_contactUsSendMessage_withIncorrectEmail_thenReturn400() throws Exception {
        ContactRequestDTO requestDTO = new ContactRequestDTO("testemail.com", "titlist", "conentis");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/contact")
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void test_contactUsSendMessage_withNotProvidedTitle_thenReturn400() throws Exception {
        ContactRequestDTO requestDTO = new ContactRequestDTO("test@email.com", "", "conentis");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/contact")
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_contactUsSendMessage_withNotProvidedContent_thenReturn400() throws Exception {
        ContactRequestDTO requestDTO = new ContactRequestDTO("test@email.com", "titlesist", "");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/contact")
                        .content(asJsonString(requestDTO))
                        .contentType(CONTENT_TYPE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_getAllMessages_withAuthenticatedAndAuthorized_thenReturn200() throws Exception {
        ContactListResponseDTO responseDTO = new ContactListResponseDTO(
                UUID.randomUUID(),
                "test@email.com",
                "testTitle",
                true,
                LocalDateTime.now());

        when(contactService.getMessages()).thenReturn(Arrays.asList(responseDTO));

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/administration/contact")
                .accept(CONTENT_TYPE)).andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(contactService, times(1)).getMessages();
        assertThat(objectMapper.writeValueAsString(Arrays.asList(responseDTO)))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    public void test_getAllMessages_whenNotAuthenticated_thenReturn401() throws Exception {

        mockMvc.perform(get("/api/administration/contact")
                .accept(CONTENT_TYPE)).andExpect(status().is(401));

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "EDITOR", "AUTHOR"})
    public void test_getAllMessages_whenNotAuthorized_thenReturn403() throws Exception {

        mockMvc.perform(get("/api/administration/contact")
                .accept(CONTENT_TYPE)).andExpect(status().is(403));

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_getMessage_withAuthenticatedAndAuthorized_thenReturn200() throws Exception {
        UUID contactId = UUID.randomUUID();

        ContactResponseDTO responseDTO = new ContactResponseDTO(
                contactId,
                "test@email.com",
                "testTitle",
                "testContent",
                true,
                LocalDateTime.now());

        when(contactService.getMessage(contactId.toString())).thenReturn(responseDTO);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/administration/contact/" + contactId)
                .accept(CONTENT_TYPE)).andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(contactService, times(1)).getMessage(contactId.toString());
        assertThat(objectMapper.writeValueAsString(responseDTO))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    public void test_getMessage_whenNotAuthenticated_thenReturn401() throws Exception {

        mockMvc.perform(get("/api/administration/contact/" + UUID.randomUUID())
                .accept(CONTENT_TYPE)).andExpect(status().is(401));

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "EDITOR", "AUTHOR"})
    public void test_getMessage_whenNotAuthorized_thenReturn403() throws Exception {

        mockMvc.perform(get("/api/administration/contact/" + UUID.randomUUID())
                .accept(CONTENT_TYPE)).andExpect(status().is(403));

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = "ADMIN")
    public void test_readMessage_withAuthenticatedAndAuthorized_thenReturn200() throws Exception {
        UUID contactId = UUID.randomUUID();

        ContactResponseDTO responseDTO = new ContactResponseDTO(
                contactId,
                "test@email.com",
                "testTitle",
                "testcontact",
                true,
                LocalDateTime.now());

        when(contactService.makeRead(contactId.toString())).thenReturn(responseDTO);

        // when
        MvcResult mvcResult = mockMvc.perform(put("/api/administration/contact/read/" + contactId)
                .accept(CONTENT_TYPE)).andReturn();

        // then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(contactService, times(1)).makeRead(contactId.toString());
        assertThat(objectMapper.writeValueAsString(responseDTO))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    public void test_readMessage_whenNotAuthenticated_thenReturn401() throws Exception {

        mockMvc.perform(put("/api/administration/contact/read/" + UUID.randomUUID())
                .accept(CONTENT_TYPE)).andExpect(status().is(401));

    }

    @Test
    @WithMockUser(username = "test_user", password = "password123", roles = {"USER", "EDITOR", "AUTHOR"})
    public void test_readMessage_whenNotAuthorized_thenReturn403() throws Exception {

        mockMvc.perform(put("/api/administration/contact/read/" + UUID.randomUUID())
                .accept(CONTENT_TYPE)).andExpect(status().is(403));

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}