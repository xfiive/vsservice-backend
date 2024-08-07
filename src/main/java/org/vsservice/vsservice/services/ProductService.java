package org.vsservice.vsservice.services;

import org.springframework.stereotype.Service;
import org.vsservice.vsservice.models.Product;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    public Optional<Product> getProduct(String id) {
        return Optional.of(null);
    }

    public Optional<Product> addProduct(Product product) {
        return Optional.of(null);
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
}
