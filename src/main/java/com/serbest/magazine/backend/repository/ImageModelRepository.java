package com.serbest.magazine.backend.repository;

import com.serbest.magazine.backend.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public interface ImageModelRepository extends JpaRepository<ImageModel, UUID> {
}
