package com.shr.blog.service;

import com.shr.blog.domain.User;
import com.shr.blog.dto.UserDto;
import com.shr.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 객체를 인수로 받는 회원 정보 추가 메서드 작성
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 생성 메서드
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }

        // 새로운 사용자 객체 생성
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .build();

        log.info("Save User Email: {}", userDto.getEmail());

        return userRepository.save(user);
    }

    // 이메일로 사용자 조회
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ID로 사용자 조회
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }
}