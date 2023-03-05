package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.common.validation.StringValidationCommon;
import com.serbest.magazine.backend.dto.contact.ContactListResponseDTO;
import com.serbest.magazine.backend.dto.contact.ContactRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Contact;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.ContactMapper;
import com.serbest.magazine.backend.repository.ContactRepository;
import com.serbest.magazine.backend.service.ContactService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public ContactServiceImpl(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    @Override
    public MessageResponseDTO sendMessage(ContactRequestDTO requestDTO) {
        checkValidateAndSanitizeInput("Email", requestDTO.getEmail());
        checkValidateAndSanitizeInput("Title", requestDTO.getTitle());
        checkValidateAndSanitizeInput("Content", requestDTO.getContent());
        StringValidationCommon.common_validateEmail(requestDTO.getEmail());
        StringValidationCommon.common_validateStringLength(1, 60, requestDTO.getTitle());

        try {
            Contact contact = new Contact(requestDTO.getEmail(), requestDTO.getTitle(), requestDTO.getContent());
            contact.setRead(false);
            Contact createdContact = contactRepository.save(contact);
            return new MessageResponseDTO(createdContact.getTitle() + " başlıklı mesajınız tarafımıza başarıyla iletilmiştir.");
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, "Bir sıkıntı oluştu.Tekrar deneyin.");
        }
    }

    @Override
    public List<ContactListResponseDTO> getMessages() {
        List<Contact> contacts = contactRepository.findAllByCreateDateTime();

        return contacts
                .stream()
                .map(contactMapper::contactToContactListResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContactResponseDTO getMessage(String messageId) {
        checkValidateAndSanitizeInput("Id", messageId);
        return contactMapper
                .contactToContactResponseDTO(contactRepository
                        .findById(UUID.fromString(messageId)).orElseThrow(
                                () -> new ResourceNotFoundException("Message", "id", messageId)
                        ));
    }

    @Override
    public ContactResponseDTO makeRead(String messageId) {
        checkValidateAndSanitizeInput("Id", messageId);
        Contact contact = contactRepository.findById(UUID.fromString(messageId)).orElseThrow(
                () -> new ResourceNotFoundException("Message", "id", messageId)
        );

        try {
            contact.setRead(true);
            return contactMapper.contactToContactResponseDTO(contactRepository.save(contact));
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void checkValidateAndSanitizeInput(String fieldName, String fieldValue) {
        Assert.notNull(fieldValue, "Please , provide a valid " + fieldName + ".");
        if (fieldValue.isEmpty() || fieldValue.isBlank()) {
            throw new IllegalArgumentException("Please , provide a valid " + fieldName + ".");
        }
    }

}
