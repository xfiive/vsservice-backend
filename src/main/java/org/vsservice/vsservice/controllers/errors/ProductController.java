package org.vsservice.vsservice.controllers.errors;

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

import java.util.List;

@RestController("/api/products")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ProductController {

    private ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<Product> getProduct(@RequestParam("{id}") @NotBlank String id) {
        return this.productService.getProduct(id)
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
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product, @RequestParam("{id}") @NotBlank String id) throws VsserviceException {
        return this.productService.updateProduct(product, id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @PatchMapping("/properties")
    public ResponseEntity<Product> updateProperties(@NotNull List<String> properties, @RequestParam("{id}") @NotBlank String id) throws VsserviceException {
        return this.productService.updateProperties(properties, id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @PatchMapping("/image")
    public ResponseEntity<Product> updateImage(@NotBlank String imageBase64, @RequestParam("{id}") @NotBlank String id) throws VsserviceException {
        return this.productService.updateImage(imageBase64, id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }

    @PatchMapping("/name")
    public ResponseEntity<Product> updateName(@NotBlank String name, @RequestParam("{id}") @NotBlank String id) throws VsserviceException {
        return this.productService.updateName(name, id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new VsserviceException(new RuntimeException()));
    }
}