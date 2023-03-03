package com.serbest.magazine.backend.service.impl;

import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.entity.Category;
import com.serbest.magazine.backend.exception.CustomApplicationException;
import com.serbest.magazine.backend.mapper.CategoryMapper;
import com.serbest.magazine.backend.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    CategoryServiceImpl categoryService;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryMapper categoryMapper;


    @Test
    public void test_createCategory_withSuccess() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("siyaset");

        Category categoryMock = Mockito.mock(Category.class);
        UUID randomId = UUID.randomUUID();

        when(categoryMock.getId()).thenReturn(randomId);
        when(categoryMock.getName()).thenReturn("siyaset");
        when(categoryMapper.categoryRequestToCategory(any(CategoryRequestDTO.class))).thenReturn(categoryMock);
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryMock);
        Category category = categoryRepository.save(categoryMapper.categoryRequestToCategory(requestDTO));

        assertEquals(category.getName(), requestDTO.getName());
        assertEquals(category.getId(), randomId);
    }

    @Test
    public void test_createCategory_checkReturn() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("siyaset");

        Category categoryMock = Mockito.mock(Category.class);

        when(categoryMock.getName()).thenReturn("siyaset");
        when(categoryMapper.categoryRequestToCategory(any(CategoryRequestDTO.class))).thenReturn(categoryMock);
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryMock);
        MessageResponseDTO result = categoryService.createCategory(requestDTO);

        assertEquals(result.getMessage(), "New Category siyaset created!");
    }

    @Test
    public void test_createCategory_withMissingArg() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();

        assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.createCategory(requestDTO)
        );
    }

    @Test
    public void test_createCategory_withNullInput(){
        CategoryRequestDTO requestDTO = new CategoryRequestDTO(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.createCategory(requestDTO)
        );
    }

    @Test
    public void test_createCategory_withExceededMaxCharInput(){
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("nullnullnullnullnullnullnullnullnullnullnull");

        assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.createCategory(requestDTO)
        );
    }

    @Test
    public void test_createCategory_withError(){
        // No Save Instruction.
        assertThrows(
                CustomApplicationException.class,
                () -> categoryService.createCategory(new CategoryRequestDTO("siyaset"))
        );
    }

    @Test
    public void test_getAllCategory_withSuccess() {
        Category category = new Category(UUID.randomUUID(), "Siyaset", true);
        Category category2 = new Category(UUID.randomUUID(), "Spor", true);
        when(categoryRepository.findByActiveTrue()).thenReturn(Arrays.asList(category, category2));
        List<CategoryResponseDTO> categoryResponseDTOS = categoryService.getAllCategory();

        assertEquals(2, categoryResponseDTOS.size());
    }

    @Test
    public void test_updateCategory_withSuccess() {
        UUID randomId = UUID.randomUUID();
        Category categoryMock = new Category(randomId, "siyaset", true);

        when(categoryRepository.findById(randomId)).thenReturn(Optional.of(categoryMock));

        categoryMock.setName("Siyaset");
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryMock);
        MessageResponseDTO messageResponseDTO = categoryService.updateCategory(randomId.toString(), new CategoryRequestDTO("Siyaset"));

        assertEquals("Category with id : " + randomId.toString() + " updated with new name : Siyaset", messageResponseDTO.getMessage());
    }

    @Test
    public void test_updateCategory_withError(){
        UUID randomId = UUID.randomUUID();
        Category categoryMock = new Category(randomId, "siyaset", true);
        when(categoryRepository.findById(randomId)).thenReturn(Optional.of(categoryMock));
        // No Save Instruction.
        assertThrows(
                CustomApplicationException.class,
                () -> categoryService.updateCategory(randomId.toString(), new CategoryRequestDTO("Siyaset"))
        );
    }

    @Test
    public void test_updateCategory_withoutProvidedId(){
        assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.updateCategory(null,new CategoryRequestDTO("Siyaset"))
        );
    }

    @Test
    public void test_deleteCategory_withSuccess(){
        UUID randomId = UUID.randomUUID();
        Category categoryMock = new Category(randomId, "siyaset", true);

        when(categoryRepository.findById(randomId)).thenReturn(Optional.of(categoryMock));
        categoryMock.setActive(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryMock);
        MessageResponseDTO messageResponseDTO = categoryService.deleteCategory(randomId.toString());

        assertEquals("Category with id : " + randomId.toString() + " is deleted.", messageResponseDTO.getMessage());
    }

    @Test
    public void test_deleteCategory_withoutProvidedId(){
        assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.deleteCategory(null)
        );
    }

    @Test
    public void test_deleteCategory_withError(){
        UUID randomId = UUID.randomUUID();
        Category categoryMock = new Category(randomId, "siyaset", true);
        when(categoryRepository.findById(randomId)).thenReturn(Optional.of(categoryMock));
        // No Save Instruction.
        assertThrows(
                CustomApplicationException.class,
                () -> categoryService.deleteCategory(randomId.toString())
        );
    }
}