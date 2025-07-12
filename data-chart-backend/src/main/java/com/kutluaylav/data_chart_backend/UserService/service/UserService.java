package com.kutluaylav.data_chart_backend.UserService.service;

import com.kutluaylav.data_chart_backend.UserService.dto.request.CreateUserRequest;
import com.kutluaylav.data_chart_backend.UserService.dto.response.UserDto;
import com.kutluaylav.data_chart_backend.UserService.exception.EmailAlreadyExistsException;
import com.kutluaylav.data_chart_backend.UserService.exception.ResourceNotFoundException;
import com.kutluaylav.data_chart_backend.UserService.exception.UsernameAlreadyExistsException;
import com.kutluaylav.data_chart_backend.UserService.mapper.UserMapper;
import com.kutluaylav.data_chart_backend.UserService.model.User;
import com.kutluaylav.data_chart_backend.UserService.model.Role;
import com.kutluaylav.data_chart_backend.UserService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto register(CreateUserRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phoneNo(request.getPhoneNo())
                .authorities(Set.of(Role.ROLE_ADMIN))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .createdDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("User '{}' registered successfully", user.getUsername());
        return toDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username).stream()
                .map(userMapper::toDto)
                .toList();
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNo(user.getPhoneNo())
                .authorities(user.getAuthorities())
                .createdDate(user.getCreatedDate())
                .build();
    }

    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
