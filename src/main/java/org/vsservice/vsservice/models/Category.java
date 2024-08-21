package org.vsservice.vsservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "categories")
@Validated
public class Category {
    @Id
    @JsonProperty("id")
    private String id;

    @Field
    @JsonProperty("name")
    @NotBlank(message = "Category name cannot be empty or null")
    @Size(min = 2, message = "Category name cannot have size lesser that 2")
    private String name;

    @Field
    @JsonProperty("parentId")
    private String parentId;

    @Field
    @JsonProperty("fullPath")
    private String fullPath;

    @Field
    @JsonProperty("children")
    private List<Category> children = new ArrayList<>();

    @Field
    @JsonProperty("imageBase64")
    private String imageBase64;

    @Field
    @JsonProperty("description")
    private String description;

    synchronized public void copyCategory(@NotNull Category srcCategory, String id) {
        this.id = id;
        this.name = srcCategory.getName();
        this.parentId = srcCategory.getParentId();
        this.children = srcCategory.getChildren();
        this.description = srcCategory.getDescription();
        this.fullPath = srcCategory.getFullPath();
        this.imageBase64 = srcCategory.getImageBase64();
    }
}
