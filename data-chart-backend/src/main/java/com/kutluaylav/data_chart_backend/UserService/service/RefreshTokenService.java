package com.kutluaylav.data_chart_backend.UserService.service;


import com.kutluaylav.data_chart_backend.UserService.model.RefreshToken;
import com.kutluaylav.data_chart_backend.UserService.model.User;
import com.kutluaylav.data_chart_backend.UserService.repository.RefreshTokenRepository;
import com.kutluaylav.data_chart_backend.UserService.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

    @Value("${jwt.expirationms}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }

    public RefreshToken createRefreshToken(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Creating refresh token for user with ID: {}", userId);


        refreshTokenRepository.findByUser(user).ifPresent(existingToken -> {
            log.info("Existing refresh token found for user {}, deleting old token", userId);
            refreshTokenRepository.delete(existingToken);
        });

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info("New refresh token created with token: {}", savedToken.getToken());

        return savedToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
        return token;
    }

    public void deleteByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }
}



