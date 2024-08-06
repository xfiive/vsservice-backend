package org.vsservice.vsservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private List<String> properties;

    @Field
    @JsonProperty("imageBase64")
    private String imageBase64;

    @Field
    @JsonProperty("name")
    private String name;
}
