package org.vsservice.vsservice.controllers.error_handling;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.vsservice.vsservice.models.errors.CategoryServiceException;
import org.vsservice.vsservice.models.errors.ProductServiceException;
import org.vsservice.vsservice.models.errors.VsserviceErrorResponse;
import org.vsservice.vsservice.models.errors.VsserviceException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController {

    @Contract("_ -> new")
    private static @NotNull GlobalExceptionController.RequestData getRequestData(@NotNull ServletWebRequest request) {
        HttpServletRequest servletRequest = request.getRequest();
        String path = servletRequest.getRequestURI();
        String method = servletRequest.getMethod();
        String clientIp = servletRequest.getRemoteAddr();
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, servletRequest.getHeader(headerName));
        }
        return new RequestData(path, method, clientIp, headers);
    }

    @ExceptionHandler(VsserviceException.class)
    public ResponseEntity<ErrorResponse> handleVsserviceException(@NotNull VsserviceException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "Error on fetching data: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCauseMessage(),
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(CategoryServiceException.class)
    public ResponseEntity<ErrorResponse> handleCategoryServiceException(@NotNull CategoryServiceException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "Error on fetching data: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCauseMessage(),
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(@NotNull ProductServiceException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "Error on fetching data: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCauseMessage(),
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(@NotNull MethodArgumentNotValidException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "Error on fetching data: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCause().getMessage(),
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(@NotNull NullPointerException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.NOT_FOUND,
                "Error on fetching data: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCause().getMessage(),
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConversionNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleConversionNotSupportedException(@NotNull ConversionNotSupportedException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "Error on fetching data: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCause().getMessage(),
                exception
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(@NotNull HttpMessageNotReadableException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                "Invalid request to server: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCause().getMessage(),
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(@NotNull AccessDeniedException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.FORBIDDEN,
                "Access Denied: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCause() != null ? exception.getCause().getMessage() : null,
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(@NotNull AuthenticationException exception, WebRequest request) {
        RequestData requestData = getRequestData((ServletWebRequest) request);

        VsserviceErrorResponse errorResponse = new VsserviceErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized: " + exception.getMessage(),
                requestData.path,
                requestData.method,
                requestData.clientIp,
                requestData.headers,
                exception.getCause() != null ? exception.getCause().getMessage() : null,
                exception
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    private record RequestData(String path, String method, String clientIp, Map<String, String> headers) {
    }
}
