package com.shr.blog.controller;

import com.shr.blog.dto.CreateAccessTokenDto;
import com.shr.blog.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenDto> createNewAccessToken(
            @RequestBody CreateAccessTokenDto tokenDto){
        String newAccessToken = tokenService.createNewAccessToken(tokenDto.getRefreshToken());
        log.info("CreateAccessToken: {}", newAccessToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenDto(null, newAccessToken));
    }
}