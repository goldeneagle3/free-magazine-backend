package com.serbest.magazine.backend.service;

import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;

import java.util.List;


public interface CategoryService {
    MessageResponseDTO createCategory(CategoryRequestDTO  categoryRequestDTO);
    List<CategoryResponseDTO> getAllCategory();
    MessageResponseDTO deleteCategory(String id);
    MessageResponseDTO updateCategory(String id,CategoryRequestDTO requestDTO);

}
