package org.vsservice.vsservice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
public class AdminDto {
    @JsonProperty("name")
    @NotBlank(message = "Admin name cannot be blank")
    private String name;
}
