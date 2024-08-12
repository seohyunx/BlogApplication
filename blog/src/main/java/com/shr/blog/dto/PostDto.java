package com.shr.blog.dto;

import com.shr.blog.domain.PostEntity;
import com.shr.blog.domain.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

/**
 * 게시물 정보를 계층 간에 전달하기 위한 Dto
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * PostDto를 PostEntity로 변환하는 메서드
     *
     * @return 변환된 PostEntity 객체
     */
    public PostEntity toEntity(User user) {
        log.info("toEntity User ID: {}", user.getId());

        return PostEntity.builder()
                .title(this.title)
                .content(this.content)
                .writer(user)
                .build();
    }
}
