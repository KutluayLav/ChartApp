package com.kutluaylav.data_chart_backend.UserService.dto.response;

import com.kutluaylav.data_chart_backend.UserService.model.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String phoneNo;
    private Set<Role> authorities;
    private LocalDateTime createdDate;
}
