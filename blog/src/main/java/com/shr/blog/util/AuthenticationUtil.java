package com.shr.blog.util;

import com.shr.blog.domain.User;
import com.shr.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
/**
 * User / OAuth2User 객체에서 공통으로 사용할 인증객체 유틸리티
 */
public class AuthenticationUtil {

    private final UserService userService;

    public User getAuthenticatedUser(Authentication authentication) {
        User user = null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = (String) oAuth2User.getAttributes().get("email");
            user = userService.findByEmail(email)
                    .orElseGet(() -> {
                        String nickname = (String) oAuth2User.getAttributes().get("name");
                        return new User(null, email, nickname);
                    });
        }

        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        return user;
    }
}