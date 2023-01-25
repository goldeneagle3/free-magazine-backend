package com.serbest.magazine.backend.repository;

import com.serbest.magazine.backend.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByName(String name);

    List<Category> findByActiveTrue();
}
