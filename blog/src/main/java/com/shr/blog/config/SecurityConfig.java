package com.shr.blog.config;

import com.shr.blog.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/h2-console/**", "/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(AbstractHttpConfigurer::disable)     // CSRF 보호 비활성화
                .securityMatcher("/**")          // /** 경로로 들어오는 모든 요청에 대해 HttpSecurity 적용

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/blog/login", "/blog/signup", "/api/blog/signup", "/swagger-ui/index.html").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/blog/login")
                        .loginProcessingUrl("/blog/login")
                        .defaultSuccessUrl("/blog/posts", true)
                        .failureUrl("/blog/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/blog/logout")
                        .logoutSuccessUrl("/blog/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(false)
                        .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public static PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}