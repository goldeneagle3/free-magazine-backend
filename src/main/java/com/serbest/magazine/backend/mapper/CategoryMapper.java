package com.serbest.magazine.backend.mapper;

import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.entity.Category;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category categoryRequestToCategory(CategoryRequestDTO categoryRequestDTO){
        return new Category(categoryRequestDTO.getName(),true);
    }

    public CategoryResponseDTO categoryToCategoryResponseDTO(Category category){
        return new CategoryResponseDTO(category.getId(),category.getName(),0);
    }
}
