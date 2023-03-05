package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.contact.ContactListResponseDTO;
import com.serbest.magazine.backend.dto.contact.ContactRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Contact;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.ContactMapper;
import com.serbest.magazine.backend.repository.ContactRepository;
import com.serbest.magazine.backend.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class ContactServiceImplIntegrationTest {

    @Autowired
    ContactService contactService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactMapper contactMapper;

    @Test
    public void testIntegration_sendMessage_success() {
        ContactRequestDTO requestDTO = new ContactRequestDTO("test@email.com", "testTitle", "testContent");

        MessageResponseDTO responseDTO = contactService.sendMessage(requestDTO);

        assertEquals(responseDTO.getMessage(),
                requestDTO.getTitle() + " başlıklı mesajınız tarafımıza başarıyla iletilmiştir.");

    }

    @Test
    public void testIntegration_sendMessage_withIncorrectEmail() {
        ContactRequestDTO requestDTO = new ContactRequestDTO("testemail.com", "testTitle", "testContent");

        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.sendMessage(requestDTO)
        );
    }

    @Test
    public void testIntegration_getMessages_withSuccess() {
        contactRepository.save(new Contact(
                "test@email.com",
                "testTitle",
                "testContent"
        ));
        List<ContactListResponseDTO> responseDTOS = contactService.getMessages();

        assertEquals(responseDTOS.size(), 1);
        assertEquals(responseDTOS.get(0).getTitle(), "testTitle");
        assertNotEquals(responseDTOS.get(0).getEmail(), "testemail.com");

    }

    @Test
    public void testIntegration_getMessage_withSuccess() {

        Contact contact = contactRepository.save(new Contact( "test@email.com", "tstTitle", "testContent"));

        ContactResponseDTO responseDTO = contactService.getMessage(contact.getId().toString());

        assertEquals(responseDTO.getTitle(), "tstTitle");
    }

    @Test
    public void testIntegration_getMessage_withNotFoundMessage() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> contactService.getMessage(UUID.randomUUID().toString())
        );
    }

    @Test
    public void testIntegration_makeRead_withSuccess(){
        Contact contact = contactRepository.save(new Contact("test@test.com","tstTitle","testContent"));

        ContactResponseDTO responseDTO = contactService.makeRead(contact.getId().toString());

        assertEquals("tstTitle",responseDTO.getTitle());
        assertEquals(responseDTO.getRead(),true);

    }

    @Test
    public void testIntegration_makeRead_withResNotFound(){
        assertThrows(
                ResourceNotFoundException.class,
                () -> contactService.makeRead(UUID.randomUUID().toString())
        );
    }

    @Test
    public void testIntegration_makeRead_withoutProvidedId(){
        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.makeRead("")
        );
    }
}