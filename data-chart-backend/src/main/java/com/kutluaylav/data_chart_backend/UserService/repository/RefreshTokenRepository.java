package com.kutluaylav.data_chart_backend.UserService.repository;

import com.kutluaylav.data_chart_backend.UserService.model.RefreshToken;
import com.kutluaylav.data_chart_backend.UserService.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByUser(User user);

    Optional<RefreshToken> findByUser(User user);
}
