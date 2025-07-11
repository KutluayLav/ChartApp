package com.kutluaylav.data_chart_backend.controller;


import com.kutluaylav.data_chart_backend.dto.request.CreateUserRequest;
import com.kutluaylav.data_chart_backend.dto.request.LoginRequest;
import com.kutluaylav.data_chart_backend.dto.response.AuthResponse;
import com.kutluaylav.data_chart_backend.dto.response.SuccessResponse;
import com.kutluaylav.data_chart_backend.dto.response.UserDto;
import com.kutluaylav.data_chart_backend.model.User;
import com.kutluaylav.data_chart_backend.security.JwtTokenProvider;
import com.kutluaylav.data_chart_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<?>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()));

            String jwt = jwtTokenProvider.generateToken(authentication);
            log.info("User '{}' logged in successfully", loginRequest.getUsername());

            AuthResponse authResponse = new AuthResponse(jwt);

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


}
