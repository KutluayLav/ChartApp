package com.kutluaylav.data_chart_backend.UserService.controller;

import com.kutluaylav.data_chart_backend.UserService.dto.response.SuccessResponse;
import com.kutluaylav.data_chart_backend.UserService.dto.response.UserDto;
import com.kutluaylav.data_chart_backend.UserService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserDto>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserDto userDto = userService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(SuccessResponse.<UserDto>builder()
                .message("User info retrieved successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(userDto)
                .build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();

        return ResponseEntity.ok(SuccessResponse.<List<UserDto>>builder()
                .message("All users retrieved successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(users)
                .build());
    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<UserDto>>> searchUsersByName(@RequestParam String name) {
        List<UserDto> results = userService.searchUsersByUsername(name);

        return ResponseEntity.ok(SuccessResponse.<List<UserDto>>builder()
                .message("Search completed")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .data(results)
                .build());
    }
}
