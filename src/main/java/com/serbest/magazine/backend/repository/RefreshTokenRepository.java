package com.serbest.magazine.backend.repository;

import com.serbest.magazine.backend.entity.Author;
import com.serbest.magazine.backend.entity.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByAuthor(Author author);
}
