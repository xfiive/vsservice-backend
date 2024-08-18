package org.vsservice.vsservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vsservice.vsservice.models.Category;
import org.vsservice.vsservice.models.errors.VsserviceException;
import org.vsservice.vsservice.repositories.CategoryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category addCategory(@NotNull Category category) {
        if (category.getId() != null && categoryRepository.existsById(category.getId())) {
            throw new VsserviceException("Failed to add new category", "Such category already exists");
        }

        return categoryRepository.save(category);
    }

    public void deleteCategory(@NotNull String id) {
        if (!categoryRepository.existsById(id))
            throw new VsserviceException("Failed to delete category", "Such category does not exist");

        categoryRepository.deleteById(id);
    }

    public Category getCategory(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to find category", "Invalid id provided or category does not exist"));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(String id, Category newCategory) {
        if (!categoryRepository.existsById(id))
            throw new VsserviceException("Failed to update category", "Such category does not exist");

        newCategory.setId(id);

        return categoryRepository.save(newCategory);
    }
}
