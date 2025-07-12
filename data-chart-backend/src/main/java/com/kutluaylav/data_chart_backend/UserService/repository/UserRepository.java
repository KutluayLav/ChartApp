package com.kutluaylav.data_chart_backend.UserService.repository;

import com.kutluaylav.data_chart_backend.UserService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByUsernameContainingIgnoreCase(String username);
}
