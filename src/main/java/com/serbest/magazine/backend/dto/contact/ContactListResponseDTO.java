package com.serbest.magazine.backend.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactListResponseDTO {
    private UUID id;
    private String email;
    private String title;
    private Boolean read;
    private LocalDateTime createDateTime;
}
