package com.kutluaylav.data_chart_backend.UserService.dto.request;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
