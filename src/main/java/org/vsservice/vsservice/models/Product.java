package org.vsservice.vsservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.validation.annotation.Validated;
import org.vsservice.vsservice.models.dtos.ProductDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
@Validated
public class Product {

    @Id
    @JsonProperty("id")
    private String id;

    @Field
    @JsonProperty("properties")
    @NotNull(message = "Properties cannot be null")
    private List<String> properties;

    @Field
    @JsonProperty("imageBase64")
    private String imageBase64;

    @Field
    @JsonProperty("name")
    @NotBlank(message = "Name cannot be blank or null")
    @Size(min = 2, message = "Name cannot be blank or null")
    private String name;

    @Field
    @JsonProperty("price")
    private BigDecimal price;

    synchronized static public Product createProductFromDto(@org.jetbrains.annotations.NotNull ProductDto dto) {
        return Product.builder().name(dto.getName()).id(dto.getId()).properties(dto.getProperties()).price(dto.getPrice()).imageBase64(dto.getImageBase64()).build();
    }

    synchronized static public ProductDto createDtoFromProduct(@org.jetbrains.annotations.NotNull Product product) {
        return ProductDto.builder().name(product.getName()).id(product.getId()).properties(product.getProperties()).price(product.getPrice()).imageBase64(product.getImageBase64()).build();
    }

    synchronized public void copyProduct(@org.jetbrains.annotations.NotNull Product product, String id) {
        this.setId(id);
        this.setImageBase64(product.getImageBase64());
        this.setName(product.getName());
        this.setProperties(product.getProperties());
        this.setPrice(product.getPrice());
    }

    synchronized public void copyProductDto(@org.jetbrains.annotations.NotNull ProductDto dto) {
        if (dto.getId() != null) this.setId(dto.getId());
        if (dto.getName() != null) this.setName(dto.getName());
        if (dto.getPrice() != null) this.setPrice(dto.getPrice());
        if (dto.getProperties() != null) this.setProperties(dto.getProperties());
        if (dto.getImageBase64() != null) this.setImageBase64(dto.getImageBase64());
    }

}
