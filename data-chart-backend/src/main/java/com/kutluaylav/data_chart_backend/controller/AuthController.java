package com.kutluaylav.data_chart_backend.controller;


import com.kutluaylav.data_chart_backend.dto.request.CreateUserRequest;
import com.kutluaylav.data_chart_backend.dto.request.LoginRequest;
import com.kutluaylav.data_chart_backend.dto.request.TokenRefreshRequest;
import com.kutluaylav.data_chart_backend.dto.response.AuthResponse;
import com.kutluaylav.data_chart_backend.dto.response.SuccessResponse;
import com.kutluaylav.data_chart_backend.dto.response.UserDto;
import com.kutluaylav.data_chart_backend.exception.RefreshTokenNotFoundException;
import com.kutluaylav.data_chart_backend.model.RefreshToken;
import com.kutluaylav.data_chart_backend.model.User;
import com.kutluaylav.data_chart_backend.security.JwtTokenProvider;
import com.kutluaylav.data_chart_backend.service.RefreshTokenService;
import com.kutluaylav.data_chart_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<?>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()));

            String jwt = jwtTokenProvider.generateToken(authentication);

            User user = userService.findEntityByUsername(loginRequest.getUsername());
            String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();

            log.info("User '{}' logged in successfully", loginRequest.getUsername());

            AuthResponse authResponse = new AuthResponse(jwt, refreshToken);

            SuccessResponse<AuthResponse> successResponse = SuccessResponse.<AuthResponse>builder()
                    .message("Login successful")
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .data(authResponse)
                    .build();

            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            log.error("Login failed for user '{}': {}", loginRequest.getUsername(), e.getMessage());

            SuccessResponse<String> errorResponse = SuccessResponse.<String>builder()
                    .message("Invalid username or password")
                    .status(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .data(null)
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }



    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<UserDto>> registerUser(@Valid @RequestBody CreateUserRequest request) {
        UserDto registeredUser = userService.register(request);

        SuccessResponse<UserDto> response = SuccessResponse.<UserDto>builder()
                .message("User registered successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(registeredUser)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<String>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {

            String token = authorizationHeader.replace("Bearer ", "");

            String username = jwtTokenProvider.getUsernameFromToken(token);

            refreshTokenService.deleteByUsername(username);

            log.info("User '{}' logged out and refresh token deleted", username);

            SuccessResponse<String> response = SuccessResponse.<String>builder()
                    .message("Logout successful")
                    .status(HttpStatus.OK)
                    .timestamp(LocalDateTime.now())
                    .data("Refresh token deleted")
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());

            SuccessResponse<String> errorResponse = SuccessResponse.<String>builder()
                    .message("Logout failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .data(null)
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<AuthResponse>> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenWithUserName(user.getUsername());
                    AuthResponse authResponse = new AuthResponse(token, requestRefreshToken);
                    SuccessResponse<AuthResponse> successResponse = SuccessResponse.<AuthResponse>builder()
                            .message("Token refreshed successfully")
                            .status(HttpStatus.OK)
                            .timestamp(LocalDateTime.now())
                            .data(authResponse)
                            .build();
                    return ResponseEntity.ok(successResponse);
                })
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token is not in database!"));
    }


}
