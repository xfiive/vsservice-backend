package org.vsservice.vsservice.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsservice.vsservice.models.Product;
import org.vsservice.vsservice.models.errors.VsserviceErrorResponse;
import org.vsservice.vsservice.models.errors.VsserviceException;
import org.vsservice.vsservice.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductService {

    private final ProductRepository productRepository;

    public Optional<Product> getProduct(String id) {
        return this.productRepository.findById(id);
    }


    @Transactional
    @Retryable(value = {VsserviceException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    public Optional<Product> addProduct(@NotNull Product product) {
//        return this.productRepository.findById(product.getId())
    }

    public Optional<Product> updateProduct(Product product, String id) {
        return Optional.of(null);
    }

    public Optional<Product> updateProperties(List<String> properties, String id) {
        return Optional.of(null);
    }

    public Optional<Product> updateImage(String imageBase64, String id) {
        return Optional.of(null);
    }


    public Optional<Product> updateName(String name, String id) {
        return Optional.of(null);
    }

    @Recover
    public void throwErrorResponse(@NotNull VsserviceErrorResponse e) throws VsserviceErrorResponse {
        throw e;
    }
}
