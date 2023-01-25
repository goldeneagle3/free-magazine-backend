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

        Category category = null;
        try {
            category = categoryRepository.save(categoryMapper.categoryRequestToCategory(categoryRequestDTO));
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
        Category category = categoryRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );

        category.setActive(false);
        try {
            categoryRepository.save(category);
            return new MessageResponseDTO("Category with id : " + category.getId() + " is deleted.");
        } catch (Exception e) {
            throw new CustomApplicationException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
