package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.contact.ContactListResponseDTO;
import com.serbest.magazine.backend.dto.contact.ContactRequestDTO;
import com.serbest.magazine.backend.dto.contact.ContactResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://magazine-app.netlify.app"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping(value = "/contact")
    public ResponseEntity<MessageResponseDTO> contactUsSendMessage(@Valid @RequestBody ContactRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.sendMessage(requestDTO));
    }

    @GetMapping(value = "/administration/contact")
    public ResponseEntity<List<ContactListResponseDTO>> getAllMessages(){
        return ResponseEntity.ok(contactService.getMessages());
    }

    @GetMapping(value = "/administration/contact/{messageId}")
    public ResponseEntity<ContactResponseDTO> getMessage(@PathVariable String messageId){
        return ResponseEntity.ok(contactService.getMessage(messageId));
    }

    @PutMapping(value = "/administration/contact/read/{messageId}")
    public ResponseEntity<ContactResponseDTO> readMessage(@PathVariable String messageId){
        return ResponseEntity.ok(contactService.makeRead(messageId));
    }
}
