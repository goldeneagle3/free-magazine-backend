package com.serbest.magazine.backend.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactRequestDTO {

    @NotBlank(message = "Lütfen email adresinizi giriniz.")
    @Email(message = "Lürfen geçerli bir email addresi giriniz.")
    private String email;

    @NotBlank(message = "Lütfen en fazla 60 karakter içeren bir başlık giriniz.")
    @Length(max = 60, message = "Başlık 60 karakterden fazla olamaz.")
    private String title;

    @NotBlank(message = "Lütfen geçerli bir içerik giriniz.")
    private String content;
}
