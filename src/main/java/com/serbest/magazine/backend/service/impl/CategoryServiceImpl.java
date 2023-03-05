package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Category;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.CategoryMapper;
import com.serbest.magazine.backend.repository.CategoryRepository;
import com.serbest.magazine.backend.repository.PostRepository;
import com.serbest.magazine.backend.service.CategoryService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public MessageResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Assert.notNull(categoryRequestDTO.getName(), "Please , provide a valid category name.");
        checkValidateAndSanitizeInput(categoryRequestDTO.getName());
        try {
            Category category = categoryRepository.save(categoryMapper.categoryRequestToCategory(categoryRequestDTO));
            return new MessageResponseDTO("New Category " + category.getName() + " created!");
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory() {
        List<Category> categories = categoryRepository.findByActiveTrue();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponseDTO updateCategory(String id, CategoryRequestDTO requestDTO) {
        Assert.notNull(id, "Please , provide a valid category id.");
        checkValidateAndSanitizeInput(requestDTO.getName());
        Category category = categoryRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );

        category.setName(requestDTO.getName());

        try {
            Category updatedcat = categoryRepository.save(category);

            return new MessageResponseDTO("Category with id : " + id + " updated with new name : " + updatedcat.getName());
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public MessageResponseDTO deleteCategory(String id) {
        Assert.notNull(id, "Please , provide a valid category id.");

        Category category = categoryRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id)
        );

        try {
            category.setActive(false);
            Category categoryDeleted = categoryRepository.save(category);
            return new MessageResponseDTO("Category with id : " + categoryDeleted.getId() + " is deleted.");
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void checkValidateAndSanitizeInput(String categoryName) {
        if (categoryName.isEmpty() || categoryName.isBlank() || categoryName.length() > 20) {
            throw new IllegalArgumentException("Please , provide a valid category less than 20 characters.");
        }
    }
}
