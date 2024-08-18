package org.vsservice.vsservice.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vsservice.vsservice.models.Category;
import org.vsservice.vsservice.services.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/search")
    public ResponseEntity<Category> getCategory(@RequestParam("id") @NotBlank String id) {
        return new ResponseEntity<>(this.categoryService.getCategory(id), HttpStatus.OK);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(this.categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody @NotBlank Category category) {
        return new ResponseEntity<>(this.categoryService.addCategory(category), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@RequestParam("id") @NotBlank String id) {
        this.categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Category> updateCategory(@RequestParam("id") @NotBlank String id, @RequestBody Category category) {
        return new ResponseEntity<>(this.categoryService.updateCategory(id, category), HttpStatus.OK);
    }
}
