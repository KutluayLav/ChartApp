package com.kutluaylav.data_chart_backend.service;

import com.kutluaylav.data_chart_backend.dto.request.CreateUserRequest;
import com.kutluaylav.data_chart_backend.dto.response.UserDto;

public interface IUserService {

    UserDto register(CreateUserRequest request);
}
