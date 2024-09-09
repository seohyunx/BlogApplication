package com.shr.blog.domain;

import com.shr.blog.dto.PostDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)  // Spring Data JPA의 Auditing 기능 (생성, 수정 감지)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // ID 자동 증가
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "title", nullable = false)               // 'title' 이라는 not null 컬럼과 매핑
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    // 생성 시간, 수정 시간 추가
    @CreatedDate        // 엔티티가 생성될 때 생성 시간 저장
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate   // 엔티티가 수정될 때 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder            // 빌더 패턴으로 객체 생성
    public PostEntity(String title, String content, User writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    // update: 요청받은 내용으로 값을 수정하는 메서드
    public PostEntity update(String title, String content, User writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;

        return this;
    }

    /**
     * PostDto 객체로 변환
     *
     * @param postEntity 변환할 PostEntity 객체
     * @return 빌더 패턴으로 PostDto 객체를 생성해서 반환
     */
    public static PostDto toDto(PostEntity postEntity) {
        return PostDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .writer(postEntity.getWriter())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .build();
    }
}
