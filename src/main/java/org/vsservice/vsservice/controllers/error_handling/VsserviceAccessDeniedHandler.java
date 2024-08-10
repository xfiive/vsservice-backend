package org.vsservice.vsservice.controllers.error_handling;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VsserviceAccessDeniedHandler implements AccessDeniedHandler {

    private final GlobalExceptionController globalExceptionController;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, @NotNull AccessDeniedException accessDeniedException)  {
        throw accessDeniedException;
    }
}
