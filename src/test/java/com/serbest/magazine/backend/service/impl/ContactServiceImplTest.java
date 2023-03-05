package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactListResponseDTO;
import com.serbest.magazine.backend.dto.contact.ContactRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactResponseDTO;
import com.serbest.magazine.backend.entity.Category;
import com.serbest.magazine.backend.entity.Contact;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.ContactMapper;
import com.serbest.magazine.backend.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @InjectMocks
    ContactServiceImpl contactService;

    @Mock
    ContactRepository contactRepository;

    @Mock
    ContactMapper contactMapper;


    @Test
    public void test_sendMessage_withSuccess() {
        ContactRequestDTO requestDTO = new ContactRequestDTO("test@email.com", "testTitle", "testContent");
        Contact contactMock = Mockito.mock(Contact.class);

        when(contactMock.getTitle()).thenReturn(requestDTO.getTitle());
        when(contactRepository.save(any(Contact.class))).thenReturn(contactMock);
        Contact contact = contactRepository.save(
                new Contact("testEmail", "testTitle", "testContent")
        );

        assertEquals(contact.getTitle(), requestDTO.getTitle());
    }

    @Test
    public void test_sendMessage_withMissingArg() {
        ContactRequestDTO requestDTO = new ContactRequestDTO("testEmail", null, "testContent");

        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.sendMessage(requestDTO)
        );
    }

    @Test
    public void test_sendMessage_checkEmailValidation() {
        ContactRequestDTO requestDTO = new ContactRequestDTO("testEmail", "testTitle", "testContent");

        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.sendMessage(requestDTO)
        );
    }

    @Test
    public void test_sendMessage_checkTitleValidation() {
        ContactRequestDTO requestDTO =
                new ContactRequestDTO(
                        "test@email.com",
                        "afawfawfafawfawafawfawfafawfawafawfawfafawfawafawfawfafawfawafawfawfafawfawafawfawfaf" +
                                "awfawafawfawfafawfawafawfawfafawfawafawfawfafawfawafawfawfafawfawafawfawfafawfaw",
                        "testContent");

        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.sendMessage(requestDTO)
        );
    }

    @Test
    public void test_sendMessage_withError() {
        ContactRequestDTO requestDTO = new ContactRequestDTO("test@email.com", "testTitle", "testContent");

        // No Save Instruction.
        assertThrows(
                CustomApplicationException.class,
                () -> contactService.sendMessage(requestDTO)
        );
    }

    @Test
    public void test_getMessages_withSuccess() {
        Contact contact1 = new Contact("test1@email.com", "testTitle", "testContent");
        Contact contact2 = new Contact("test2@email.com", "testTitle", "testContent");
        when(contactRepository.findAllByCreateDateTime()).thenReturn(Arrays.asList(contact1, contact2));
        List<ContactListResponseDTO> responseDTOS = contactService.getMessages();

        assertEquals(2, responseDTOS.size());
    }

    @Test
    public void test_getMessage_withSuccess() {
        UUID randomId = UUID.randomUUID();
        Contact contact1 = new Contact("test1@email.com", "testTitle", "testContent");

        when(contactRepository.findById(randomId)).thenReturn(Optional.of(contact1));
        when(contactMapper.contactToContactResponseDTO(contact1))
                .thenReturn(new ContactResponseDTO(
                        randomId,
                        contact1.getEmail(),
                        contact1.getTitle(),
                        contact1.getContent(),
                        true,
                        LocalDateTime.now()));
        ContactResponseDTO responseDTO = contactService.getMessage(randomId.toString());

        assertEquals(responseDTO.getContent(), contact1.getContent());
        assertEquals(responseDTO.getId(), randomId);
        assertEquals(responseDTO.getRead(), true);
    }

    @Test
    public void test_makeRead_withSuccess() {
        UUID randomId = UUID.randomUUID();
        Contact contact = new Contact(randomId,
                "test1@email.com",
                "testTitle",
                "testContent",
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(contactRepository.findById(randomId)).thenReturn(Optional.of(contact));

        when(contactMapper.contactToContactResponseDTO(contact))
                .thenReturn(new ContactResponseDTO(
                        randomId,
                        contact.getEmail(),
                        contact.getTitle(),
                        contact.getContent(),
                        true,
                        LocalDateTime.now()));
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        ContactResponseDTO responseDTO = contactService.makeRead(randomId.toString());

        assertEquals(responseDTO.getRead(), true);
    }

    @Test
    public void test_makeRead_withoutSaveError() {
        UUID randomId = UUID.randomUUID();
        Contact contact = new Contact("test", "test", "test");
        ContactResponseDTO responseDTO = new ContactResponseDTO(
                randomId,
                contact.getEmail(),
                contact.getTitle(),
                contact.getContent(),
                true,
                LocalDateTime.now());

        when(contactRepository.findById(randomId)).thenReturn(Optional.of(contact));
        when(contactMapper.contactToContactResponseDTO(contact)).thenReturn(responseDTO);
        assertThrows(
                CustomApplicationException.class,
                () -> contactService.makeRead(randomId.toString())
        );
    }

    @Test
    public void test_makeRead_withResourceNotFound(){
        UUID randomId = UUID.randomUUID();

        assertThrows(
                ResourceNotFoundException.class,
                () -> contactService.makeRead(randomId.toString())
        );
    }

    @Test
    public void test_makeRead_withMissingId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.makeRead("")
        );
    }

    @Test
    public void test_makeRead_withNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> contactService.makeRead(null)
        );
    }

}