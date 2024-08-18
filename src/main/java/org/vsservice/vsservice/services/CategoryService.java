package org.vsservice.vsservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsservice.vsservice.models.Category;
import org.vsservice.vsservice.models.Product;
import org.vsservice.vsservice.models.errors.VsserviceException;
import org.vsservice.vsservice.repositories.CategoryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @Retryable(retryFor = VsserviceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "categories", key = "#category.name")
    public Category addCategory(@NotNull Category category) {
        if (category.getId() != null && categoryRepository.existsById(category.getId())) {
            throw new VsserviceException("Failed to add new category", "Such category already exists");
        }

        return categoryRepository.save(category);
    }

    @Transactional
    @Retryable(retryFor = VsserviceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CacheEvict(value = "categories", key = "#id")
    public void deleteCategory(@NotNull String id) {
        if (!categoryRepository.existsById(id))
            throw new VsserviceException("Failed to delete category", "Such category does not exist");

        categoryRepository.deleteById(id);
    }

    @Transactional
    @Retryable(retryFor = VsserviceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @Cacheable(value = "categories", key = "#id")
    public Category getCategory(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to find category", "Invalid id provided or category does not exist"));
    }

    @Transactional
    @Retryable(retryFor = VsserviceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @Cacheable(value = "categories", key = "getMethodName()")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    @Retryable(retryFor = VsserviceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "categories", key = "#id")
    public Category updateCategory(String id, Category newCategory) {
        if (!categoryRepository.existsById(id))
            throw new VsserviceException("Failed to update category", "Such category does not exist");

        newCategory.setId(id);

        return categoryRepository.save(newCategory);
    }

    @Recover
    @SuppressWarnings("unused")
    public Category recoverVsserviceException(@NotNull VsserviceException e, String id) {
        log.error("Failed after retries. Exception: {}", e.getMessage());
        return null;
    }

    @Recover
    @SuppressWarnings("unused")
    public Category recoverFromGetCategory(@NotNull VsserviceException e, String id) {
        log.error("Failed to retrieve category with id: {} after retries. Exception: {}", id, e.getMessage());
        return null;
    }

    @Recover
    @SuppressWarnings("unused")
    public Category recoverFromAddCategory(@NotNull VsserviceException e, String id) {
        log.error("Failed to add category with id: {} after retries. Exception: {}", id, e.getMessage());
        return null;
    }

    @Recover
    @SuppressWarnings("unused")
    public Category recoverFromUpdateCategory(@NotNull VsserviceException e, Product product, String id) {
        log.error("Failed to update category with id: {} after retries. Exception: {}", id, e.getMessage());
        return null;
    }

}
