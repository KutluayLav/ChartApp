package com.kutluaylav.data_chart_backend.UserService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;

}
