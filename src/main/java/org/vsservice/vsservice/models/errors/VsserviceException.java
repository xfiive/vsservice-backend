package org.vsservice.vsservice.models.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@JsonIgnoreProperties({"stackTrace", "localization"})
public class VsserviceException {
    private final String message;
    private final String causeMessage;

    public VsserviceException(@NotNull Throwable cause) {
        this.message = cause.getMessage();
        this.causeMessage = cause.getCause() != null ? cause.getCause().getMessage() : null;
    }

}
