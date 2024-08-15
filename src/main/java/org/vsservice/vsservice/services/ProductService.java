package org.vsservice.vsservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public Optional<Product> getProduct(String id) {
        return this.productRepository.findById(id);
    }

    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public Optional<Product> addProduct(@NotNull Product product) {
        if (product.getId() != null && productRepository.findById(product.getId()).isPresent()) {
            throw new VsserviceException("Failed to add new product", "Such product already exists");
        }
        return Optional.of(productRepository.save(product));
    }

    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public Optional<Product> updateProduct(Product product, String id) {
        Optional<Product> existingProductOpt = productRepository.findById(id);

        existingProductOpt.ifPresent(existingProduct -> {
            existingProduct.copyProduct(product, id);
            productRepository.save(existingProduct);
        });

        return Optional.ofNullable(existingProductOpt.orElseThrow(() -> new VsserviceException("Failed to update product", "Product not found")));
    }

    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public Optional<Product> updateProperties(List<String> properties, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product properties", "Product not found"));

        existingProduct.setProperties(properties);
        return Optional.of(productRepository.save(existingProduct));
    }

    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public Optional<Product> updateImage(String imageBase64, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product image", "Product not found"));

        existingProduct.setImageBase64(imageBase64);
        return Optional.of(productRepository.save(existingProduct));
    }

    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public Optional<Product> updateName(String name, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product name", "Product not found"));

        existingProduct.setName(name);
        return Optional.of(productRepository.save(existingProduct));
    }

    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public Optional<Product> updatePrice(BigDecimal price, String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to update product price", "Product not found"));

        existingProduct.setPrice(price);
        return Optional.of(productRepository.save(existingProduct));
    }

    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 250))
    public void deleteProduct(String id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new VsserviceException("Failed to delete product", "Product not found"));

        productRepository.delete(existingProduct);
    }

    @Recover
    public Optional<Product> recoverVsserviceException(@NotNull VsserviceException e, String id) {
        log.error("Failed after retries. Exception: {}", e.getMessage());
        return Optional.empty();
    }

    @Recover
    public Optional<Product> recoverFromGetProduct(@NotNull VsserviceException e, String id) {
        log.error("Failed to retrieve product with id: {} after retries. Exception: {}", id, e.getMessage());
        return Optional.empty();
    }

    @Recover
    public Optional<Product> recoverFromAddProduct(@NotNull VsserviceException e, Product product) {
        log.error("Failed to add product after retries. Exception: {}", e.getMessage());
        return Optional.empty();
    }

    @Recover
    public Optional<Product> recoverFromUpdateProduct(@NotNull VsserviceException e, Product product, String id) {
        log.error("Failed to update product with id: {} after retries. Exception: {}", id, e.getMessage());
        return Optional.empty();
    }

}
