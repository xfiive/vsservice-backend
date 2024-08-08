package org.vsservice.vsservice.models.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({"stackTrace", "localization"})
public class VsserviceException extends RuntimeException {
    private final String message;
    private final String causeMessage;

    public VsserviceException(@NotNull Throwable cause) {
        this.message = cause.getMessage();
        this.causeMessage = cause.getCause() != null ? cause.getCause().getMessage() : null;
    }

    public VsserviceException(String message, String causeMessage) {
        this.message = message;
        this.causeMessage = causeMessage;
    }

}
