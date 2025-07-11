package com.kutluaylav.data_chart_backend.repository;

import com.kutluaylav.data_chart_backend.model.RefreshToken;
import com.kutluaylav.data_chart_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    Optional<RefreshToken> findByUser(User user);
}
