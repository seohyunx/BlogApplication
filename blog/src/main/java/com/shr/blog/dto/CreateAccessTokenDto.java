package com.shr.blog.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccessTokenDto {
    private String refreshToken;
    private String accessToken;

    @Builder
    public void createAccessTokenRequest(String refreshToken) {}

    @Builder
    public CreateAccessTokenDto(String accessToken) {}

}