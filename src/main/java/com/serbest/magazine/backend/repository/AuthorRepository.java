package com.serbest.magazine.backend.repository;

import com.serbest.magazine.backend.dto.author.AuthorListResponseDTO;
import com.serbest.magazine.backend.dto.user.AuthorResponseDTO;
import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    Optional<Author> findByEmail(String email);

    Optional<Author> findByUsernameOrEmail(String username, String email);

    Optional<Author> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<Author> findByRolesIn(List<Role> roles);
}
