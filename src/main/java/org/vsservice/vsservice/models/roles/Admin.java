package org.vsservice.vsservice.models.roles;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "admins")
public class Admin {

    @Id
    @JsonProperty("id")
    private String id;

    @Field
    @JsonProperty("username")
    @NotBlank(message = "Username cannot be null")
    @Indexed(unique = true)
    private String username;

    @Field
    @JsonProperty("password")
    @NotBlank(message = "Password cannot be null")
    private String password;
}
