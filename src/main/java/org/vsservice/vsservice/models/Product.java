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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
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
    @NotBlank(message = "Product image cannot be blank or null")
    private String imageBase64;

    @Field
    @JsonProperty("name")
    @NotBlank(message = "Name cannot be blank or null")
    @Size(min = 2, message = "Name cannot be blank or null")
    private String name;

    @Field
    @JsonProperty("price")
    @NotNull(message = "Price cannot be empty or null")
    private BigDecimal price;


    synchronized public void copy(@org.jetbrains.annotations.NotNull Product product, String id) {
        this.setId(id);
        this.setImageBase64(product.getImageBase64());
        this.setName(product.getName());
        this.setProperties(product.getProperties());
        this.setPrice(product.getPrice());
    }

}
