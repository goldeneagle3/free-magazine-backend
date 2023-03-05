package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Category;
import com.serbest.magazine.backend.exception.ResourceNotFoundException;
import com.serbest.magazine.backend.mapper.CategoryMapper;
import com.serbest.magazine.backend.repository.CategoryRepository;
import com.serbest.magazine.backend.service.CategoryService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class CategoryServiceImplIntegrationTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    @Test
    public void testIntegration_createCategory_success() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO(generateRandomCategoryName());

        MessageResponseDTO responseDTO = categoryService.createCategory(requestDTO);
        Optional<Category> category = categoryRepository.findByName(requestDTO.getName());

        assertEquals(category.get().getName(), requestDTO.getName());
        assertEquals(responseDTO.getMessage(), "New Category " + category.get().getName() + " created!");
    }

    @Test
    public void testIntegration_getAllCategory_success() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO(generateRandomCategoryName());

        categoryRepository.save(categoryMapper.categoryRequestToCategory(requestDTO));
        List<CategoryResponseDTO> categories = categoryService.getAllCategory();

        assertEquals(categories.size(), 1);
    }

    @Test
    public void testIntegration_updateCategory_success() {
        String categoryName = generateRandomCategoryName();
        CategoryRequestDTO requestDTO = new CategoryRequestDTO(categoryName);
        CategoryRequestDTO requestUpdateDTO = new CategoryRequestDTO("Siyaset");

        Category category = categoryRepository.save(categoryMapper.categoryRequestToCategory(requestDTO));
        MessageResponseDTO responseDTO = categoryService.updateCategory(category.getId().toString(), requestUpdateDTO);

        assertEquals(
                responseDTO.getMessage(),
                "Category with id : " + category.getId() + " updated with new name : " + requestUpdateDTO.getName());
    }

    @Test
    public void testIntegration_updateCategory_WrongGivenId() {
        String categoryName = generateRandomCategoryName();
        CategoryRequestDTO requestDTO = new CategoryRequestDTO(categoryName);
        CategoryRequestDTO requestUpdateDTO = new CategoryRequestDTO("Siyaset");

        Category category = categoryRepository.save(categoryMapper.categoryRequestToCategory(requestDTO));
        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.updateCategory(UUID.randomUUID().toString(), requestUpdateDTO)
        );
    }

    @Test
    public void testIntegration_deleteCategory_success() {
        String categoryName = generateRandomCategoryName();
        CategoryRequestDTO requestDTO = new CategoryRequestDTO(categoryName);

        Category category = categoryRepository.save(categoryMapper.categoryRequestToCategory(requestDTO));
        MessageResponseDTO responseDTO = categoryService.deleteCategory(category.getId().toString());

        assertEquals(
                responseDTO.getMessage(),
                "Category with id : " + category.getId() + " is deleted.");
    }

    @Test
    public void testIntegration_deleteCategory_WrongGivenId() {
        String categoryName = generateRandomCategoryName();
        CategoryRequestDTO requestDTO = new CategoryRequestDTO(categoryName);

        categoryRepository.save(categoryMapper.categoryRequestToCategory(requestDTO));
        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.deleteCategory(UUID.randomUUID().toString())
        );
    }

    @Test
    public void testIntegration_checkFindByCategoryIdFail() {
        Optional<Category> category = categoryRepository.findById(UUID.randomUUID());

        assertThrows(
                NoSuchElementException.class,
                () -> category.get()
        );
    }

    public String generateRandomCategoryName() {

        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        return generatedString;
    }


}