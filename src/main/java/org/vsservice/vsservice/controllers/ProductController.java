package org.vsservice.vsservice.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vsservice.vsservice.models.Product;
import org.vsservice.vsservice.models.errors.VsserviceException;
import org.vsservice.vsservice.services.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<Product> getProduct(@RequestParam("id") @NotBlank String id) {
        return Optional.ofNullable(this.productService.getProduct(id))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) throws VsserviceException {
        return this.productService.addProduct(product)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product, @RequestParam("id") @NotBlank String id) throws VsserviceException {
        return Optional.ofNullable(this.productService.updateProduct(product, id))
                .map(updatedProduct -> new ResponseEntity<>(updatedProduct, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException("Failed to update product", "Product not found"));
    }


    @PatchMapping("/properties")
    public ResponseEntity<Product> updateProperties(@RequestBody @NotNull List<String> properties, @RequestParam("id") @NotBlank String id) throws VsserviceException {
        return Optional.ofNullable(this.productService.updateProperties(properties, id))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @PatchMapping("/image")
    public ResponseEntity<Product> updateImage(@RequestParam("imageBase64") @NotBlank String imageBase64, @RequestParam("id") @NotBlank String id) throws VsserviceException {
        return Optional.ofNullable(this.productService.updateImage(imageBase64, id))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @PatchMapping("/name")
    public ResponseEntity<Product> updateName(@RequestParam("name") @NotBlank String name, @RequestParam("id") @NotBlank String id) throws VsserviceException {
        return Optional.ofNullable(this.productService.updateName(name, id))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @PatchMapping("/price")
    public ResponseEntity<Product> updatePrice(@RequestParam("price") @NotNull BigDecimal price, @RequestParam("id") @NotBlank String id) throws VsserviceException {
        return Optional.ofNullable(this.productService.updatePrice(price, id))
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestParam("id") @NotBlank String id) {
        this.productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
