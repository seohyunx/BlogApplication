package com.shr.blog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * User 엔티티
 * -> User 테이블과 매핑될 엔티티 클래스
 */
@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "writer")
    private List<PostEntity> postEntities;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * getAuthorities(): 사용자가 가진 권한(역할)을 반환
     * -> Spring Security가 이 정보를 사용해서 접근 제어를 수행한다.
     *
     * @return SimpleGrantedAuthority(role)
     * -> GrantedAuthority 인터페이스의 구현체: SimpleGrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 계정 만료 여부를 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부를 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명의 만료 여부를 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정의 활성화 여부를 반환
    @Override
    public boolean isEnabled() {
        return true;
    }
}
