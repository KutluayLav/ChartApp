package com.kutluaylav.data_chart_backend.exception;

import com.kutluaylav.data_chart_backend.dto.response.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiException<Void>> handleNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());

        ApiException<Void> apiException = ApiException.<Void>builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiException<Void>> handleUsernameExists(UsernameAlreadyExistsException ex) {
        log.warn("Conflict error: {}", ex.getMessage());
        ApiException<Void> apiException = ApiException.<Void>builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiException<Void>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Bad credentials: {}", ex.getMessage());

        ApiException<Void> apiException = ApiException.<Void>builder()
                .message("Invalid username or password")
                .status(HttpStatus.UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        log.warn("Validation failed: {}", errors);
        ApiException<Map<String, String>> apiException = ApiException.<Map<String, String>>builder()
                .message("Validation error")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .data(errors)
                .build();
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException<Void>> handleGenericException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);

        ApiException<Void> apiException = ApiException.<Void>builder()
                .message("Internal server error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ApiException<String>> handleRefreshTokenNotFound(RefreshTokenNotFoundException ex) {
        ApiException<String> response = ApiException.<String>builder()
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiException<String>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiException<String> response = ApiException.<String>builder()
                .message("You do not have permission to access this resource.")
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiException<String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ApiException<String> response = ApiException.<String>builder()
                .message("You do not have permission to access this resource.")
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
