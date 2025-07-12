package com.kutluaylav.data_chart_backend.UserService.service;

import com.kutluaylav.data_chart_backend.UserService.dto.request.CreateUserRequest;
import com.kutluaylav.data_chart_backend.UserService.dto.response.UserDto;

import java.util.List;

public interface IUserService {

    UserDto register(CreateUserRequest request);
    UserDto getUserByUsername(String username);
    List<UserDto> getAllUsers();
    List<UserDto> searchUsersByUsername(String username);

}
