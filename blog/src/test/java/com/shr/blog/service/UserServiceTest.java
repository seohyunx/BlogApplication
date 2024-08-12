package com.shr.blog.service;

import com.shr.blog.domain.User;
import com.shr.blog.dto.UserDto;
import com.shr.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("save: User 회원가입 성공")
    void save() {
        // given
        UserDto userDto = UserDto.builder()
                .email("test@gmail.com")
                .password("test_password")
                .nickname("test_nickname")
                .build();

        // when
        User savedUserId = userService.save(userDto);

        // then
        User savedUser = userRepository.findById(savedUserId).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@gmail.com");
        assertThat(savedUser.getNickname()).isEqualTo("test_nickname");
        assertThat(passwordEncoder.matches("test_password", savedUser.getPassword())).isTrue();
    }
}
