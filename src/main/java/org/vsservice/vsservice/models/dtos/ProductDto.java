package org.vsservice.vsservice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.vsservice.vsservice.models.Product;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Validated
public class ProductDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("properties")
    @NotNull(message = "Properties cannot be null")
    private List<String> properties;

    @JsonProperty("imageBase64")
    private String imageBase64;

    @JsonProperty("name")
    @NotBlank(message = "Name cannot be blank or null")
    @Size(min = 2, message = "Name cannot be blank or null")
    private String name;

    @JsonProperty("price")
    private BigDecimal price;

//    synchronized static Product createDtoFromProduct(@org.jetbrains.annotations.NotNull Product product) {
//        Product newProduct = new Product();
//        if (dto.getId() != null) newProduct.setId(dto.getId());
//        if (dto.getName() != null) newProduct.setName(dto.getName());
//        if (dto.getPrice() != null) newProduct.setPrice(dto.getPrice());
//        if (dto.getProperties() != null) newProduct.setProperties(dto.getProperties());
//        if (dto.getImageBase64() != null) newProduct.setImageBase64(dto.getImageBase64());
//        return newProduct;
//    }
}
