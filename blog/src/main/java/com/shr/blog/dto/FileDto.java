package com.shr.blog.dto;

import com.shr.blog.domain.PostFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileDto {
    private String fileName;    // 파일 이름
    private String filePath;    // 파일 경로
    private String uuid;        // uuid (랜덤키)
    private String fileType;    // 파일 포멧
    private Long fileSize;     // 파일 크기
    private Long postId;

    public PostFile toEntity() {
        return PostFile.builder()
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }
}
