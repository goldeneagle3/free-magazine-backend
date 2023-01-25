package com.serbest.magazine.backend.controller;

import com.serbest.magazine.backend.dto.category.CategoryRequestDTO;
import com.serbest.magazine.backend.dto.category.CategoryResponseDTO;
import com.serbest.magazine.backend.dto.category.CategoryUpdateRequestDTO;
import com.serbest.magazine.backend.dto.general.MessageResponseDTO;
import com.serbest.magazine.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/administration/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO){
        return ResponseEntity.ok(categoryService.createCategory(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> updateCategory(@PathVariable String id, @RequestBody CategoryRequestDTO updateRequestDTO){
        return ResponseEntity.ok(categoryService.updateCategory(id,updateRequestDTO));
    }

    @PutMapping("deleteCategory/{id}")
    public ResponseEntity<MessageResponseDTO> deleteCategoryById(@PathVariable String id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
