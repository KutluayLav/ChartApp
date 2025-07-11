package com.kutluaylav.data_chart_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiException<T> {
    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;
    private T data; // Extra payload
}
