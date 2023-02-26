package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.contact.ContactListResponseDTO;
import com.serbest.magazine.backend.dto.contact.ContactRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;

import java.util.List;

public interface ContactService {
    MessageResponseDTO sendMessage(ContactRequestDTO requestDTO);

    List<ContactListResponseDTO> getMessages();

    ContactResponseDTO makeRead(String messageId);

    ContactResponseDTO getMessage(String messageId);
}
