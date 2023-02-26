package com.serbest.magazine.backend.mapper;


import com.serbest.magazine.backend.dto.contact.ContactListResponseDTO;
import com.serbest.magazine.backend.dto.contact.ContactResponseDTO;
import com.serbest.magazine.backend.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactResponseDTO contactToContactResponseDTO(Contact contact) {
        return ContactResponseDTO.builder()
                .id(contact.getId())
                .email(contact.getEmail())
                .title(contact.getTitle())
                .content(contact.getContent())
                .read(contact.getRead())
                .createDateTime(contact.getCreateDateTime())
                .build();
    }

    public ContactListResponseDTO contactToContactListResponseDTO(Contact contact) {
        return ContactListResponseDTO.builder()
                .id(contact.getId())
                .email(contact.getEmail())
                .title(contact.getTitle())
                .read(contact.getRead())
                .createDateTime(contact.getCreateDateTime())
                .build();
    }
}
