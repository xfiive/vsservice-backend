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
import org.vsservice.vsservice.models.Product;
import org.vsservice.vsservice.models.errors.VsserviceException;
import org.vsservice.vsservice.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    @Retryable(retryFor = VsserviceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @Cacheable(value = "products", key = "#id")
    public Product getProduct(String id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to add new product", "Such product already exists"));
    }


    @Transactional
    @Retryable(retryFor = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "products", key = "#product.name")
    public Optional<Product> addProduct(@NotNull Product product) {
        if (product.getId() != null && productRepository.findById(product.getId()).isPresent()) {
            throw new VsserviceException("Failed to add new product", "Such product already exists");
        }

        return Optional.of(productRepository.save(product));
    }

    @Transactional
    @Retryable(retryFor = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "products", key = "#id")
    public Product updateProduct(Product product, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product", "Product not found"));

        existingProduct.copyProduct(product, id);
        productRepository.save(existingProduct);

        return existingProduct;
    }


    @Transactional
    @Retryable(retryFor = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "products", key = "#id")
    public Product updateProperties(List<String> properties, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product properties", "Product not found"));

        existingProduct.setProperties(properties);
        return productRepository.save(existingProduct);
    }


    @Transactional
    @Retryable(retryFor = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "products", key = "#id")
    public Product updateImage(String imageBase64, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product image", "Product not found"));

        existingProduct.setImageBase64(imageBase64);
        return productRepository.save(existingProduct);
    }

    @Transactional
    @Retryable(retryFor = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "products", key = "#id")
    public Product updateName(String name, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product name", "Product not found"));

        existingProduct.setName(name);
        return productRepository.save(existingProduct);
    }

    @Transactional
    @Retryable(retryFor = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CachePut(value = "products", key = "#id")
    public Product updatePrice(BigDecimal price, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product price", "Product not found"));

        existingProduct.setPrice(price);
        return productRepository.save(existingProduct);
    }

    @Transactional
    @Retryable(retryFor = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to delete product", "Product not found"));

        productRepository.delete(existingProduct);
    }

    @Recover
    @SuppressWarnings("unused")
    public Product recoverVsserviceException(@NotNull VsserviceException e, String id) {
        log.error("Failed after retries. Exception: {}", e.getMessage());
        return null;
    }

    @Recover
    @SuppressWarnings("unused")
    public Product recoverFromGetProduct(@NotNull VsserviceException e, String id) {
        log.error("Failed to retrieve product with id: {} after retries. Exception: {}", id, e.getMessage());
        return null;
    }

    @Recover
    @SuppressWarnings("unused")
    public Product recoverFromAddProduct(@NotNull VsserviceException e, Product product) {
        log.error("Failed to add product after retries. Exception: {}", e.getMessage());
        return null;
    }

    @Recover
    @SuppressWarnings("unused")
    public Product recoverFromUpdateProduct(@NotNull VsserviceException e, Product product, String id) {
        log.error("Failed to update product with id: {} after retries. Exception: {}", id, e.getMessage());
        return null;
    }

}
