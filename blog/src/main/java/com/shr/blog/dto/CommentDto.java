package com.shr.blog.dto;

import com.shr.blog.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private String writerNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // public static: 정적 메서드
    // -> 객체 생성 없이 호출 가능한 메서드. 즉, 생성 메서드를 의미
    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writerNickname(comment.getWriter().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
