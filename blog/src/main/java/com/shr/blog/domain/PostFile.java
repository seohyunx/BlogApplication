package com.shr.blog.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath;    // 저장된 파일 경로

    @Column(nullable = false)
    private String fileName;    // 파일 이름

    @Column(nullable = false)
    private String uuid;        // uuid (랜덤키)

    @Column(nullable = false)
    private String fileType;    // 파일 포멧

    @Column(nullable = false)
    private Long fileSize;      // 파일 크기

    // 게시물과 연관 관계 설정 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    // 파일 엔티티 생성 메서드
    @Builder
    public PostFile(String filePath, String fileName, String uuid, String fileType, Long fileSize, PostEntity post) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.post = post;
    }
}
