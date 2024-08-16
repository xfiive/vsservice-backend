package org.vsservice.vsservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.vsservice.vsservice.components.jwt.JwtUtil;
import org.vsservice.vsservice.models.errors.TokenRefreshException;
import org.vsservice.vsservice.models.security.RefreshToken;
import org.vsservice.vsservice.repositories.RefreshTokenRepository;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;


    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDurationMs;

    @Cacheable(value = "refreshTokens", key = "#token", unless = "#result == null")
    @SuppressWarnings("all")
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).get();
    }

    @Caching(
            evict = {@CacheEvict(value = "refreshTokens", key = "#result.token")},
            put = {@CachePut(value = "refreshTokens", key = "#result.token")}
    )
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(jwtUtil.generateRefreshToken(username));

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(@NotNull RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            evictExpiredToken(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign-in request");
        }

        return token;
    }

    @CacheEvict(value = "refreshTokens", key = "#token.token")
    public void evictExpiredToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }


    @CacheEvict(value = "refreshTokens", key = "#username")
    public int deleteByUsername(String username) {
        return refreshTokenRepository.deleteByUsername(username);
    }
}
