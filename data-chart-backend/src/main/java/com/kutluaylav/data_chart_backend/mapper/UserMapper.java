package com.kutluaylav.data_chart_backend.mapper;

import com.kutluaylav.data_chart_backend.dto.response.UserDto;
import com.kutluaylav.data_chart_backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNo(user.getPhoneNo())
                .authorities(user.getAuthorities())
                .createdDate(user.getCreatedDate())
                .build();
    }
}
