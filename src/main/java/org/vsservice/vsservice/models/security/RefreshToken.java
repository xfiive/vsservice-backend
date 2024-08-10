package org.vsservice.vsservice.models.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "refresh_tokens")
@Data
public class RefreshToken {
    @Id
    @Field
    @Indexed(unique = true)
    private String id;

    @Field
    @JsonProperty("token")
    @NotBlank(message = "Token cannot be empty")
    private String token;

    @Field
    @JsonProperty("username")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Field
    @JsonProperty("expiryDate")
    @NotNull(message = "Expiry date cannot be empty")
    private Instant expiryDate;
}
