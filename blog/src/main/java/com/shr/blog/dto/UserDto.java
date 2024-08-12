package com.shr.blog.dto;

import lombok.*;

/**
 * UserDto
 * -> 회원가입 폼에서 받아오는 데이터
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String email;
    private String password;
    private String nickname;
}