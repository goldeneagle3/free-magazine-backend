package com.serbest.magazine.backend.security;

import com.google.common.base.Strings;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.repository.AuthorRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class CheckAuthorization {

    private final AuthorRepository authorRepository;

    public CheckAuthorization(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author checkUser(Author author) throws AccessDeniedException {
        SecurityContext context = SecurityContextHolder.getContext();
        String usernameOrEmail = context.getAuthentication().getName();

        if (Strings.isNullOrEmpty(context.getAuthentication().getName())){
            throw new AccessDeniedException("You are not allowed to do that!");
        }

        Author registeredUser = authorRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail).orElseThrow(
                () -> new ResourceNotFoundException("Author", "usernameOrEmail", usernameOrEmail)
        );


        if (author != registeredUser){
            throw new AccessDeniedException("You are not allowed to do that!");
        }

        return registeredUser;

    }
}
