package com.shr.blog.service;

import com.shr.blog.config.jwt.TokenProvider;
import com.shr.blog.domain.RefreshToken;
import com.shr.blog.domain.User;
import com.shr.blog.repository.RefreshTokenRepository;
import com.shr.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        // 새로운 리프레시 토큰 생성 및 저장 로직 추가
        String newRefreshToken = tokenProvider.generateToken(user, Duration.ofDays(14));
        refreshTokenRepository.save(new RefreshToken(userId, newRefreshToken));

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}