package com.shr.blog.service;

import com.shr.blog.domain.PostEntity;
import com.shr.blog.domain.PostFile;
import com.shr.blog.domain.User;
import com.shr.blog.dto.PostDto;
import com.shr.blog.repository.FileRepository;
import com.shr.blog.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 비즈니스 로직을 처리하는 Service 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileRepository fileRepository;

    @Value("${spring.servlet.multipart.location}")  // 파일 저장 경로 (절대 경로)
    private String fileUploadPath;

    @Value("${file.upload.relative-path}")  // 파일 접근 경로 (상대 경로)
    private String fileAccessPath;

    /**
     * createPost: 게시물을 추가하는 메서드
     *
     * @param postDto 등록할 게시물의 정보
     * @return 생성된 PostEntity 객체
     */
    @Transactional
    public PostDto createPost(PostDto postDto, User user, MultipartFile[] files) throws IOException{
        log.info("Creating post with writer ID: {}", user.getId());

        PostEntity postEntity = postDto.toEntity(user);
        PostEntity savedPost = postRepository.save(postEntity);

        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    saveFile(file, savedPost);  // 파일 저장 메서드 호출
                }
            }
        }

        log.info("Created Post ID: {}", postEntity.getId());

        return PostEntity.toDto(savedPost);
    }

    /**
     * 모든 게시물을 조회하는 메서드
     *
     * @return 게시물 목록을 담은 List<PostEntity>
     */
    public List<PostDto> getAllPosts() {
        List<PostEntity> postEntities = postRepository.findAllByOrderByIdDesc();

        return postEntities.stream()
                .map(PostEntity::toDto) // PostEntity를 PostDto로 변환
                .collect(Collectors.toList());
    }

    /**
     * 하나의 게시물을 조회하는 메서드
     *
     * @param id 조회할 게시물의 ID
     * @return 조회된 PostEntity 객체
     */
    public PostDto getPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        return PostEntity.toDto(postEntity);
    }

    /**
     * 게시물을 업데이트하는 메서드
     *
     * @param id 수정할 게시물의 ID
     * @param postDto 갱신할 게시물의 정보
     * @return 수정된 PostEntity 객체
     */
    @Transactional
    public PostDto updatePost(Long id, PostDto postDto, User user, Long[] existingFileIds, MultipartFile[] files) throws IOException {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        postEntity.update(postDto.getTitle(), postDto.getContent(), user);

        // 기존 파일 삭제 처리
        List<PostFile> currentFiles = postEntity.getFiles();
        if (existingFileIds != null) {
            currentFiles.removeIf(file -> !List.of(existingFileIds).contains(file.getId()));
        } else {
            currentFiles.clear(); // 모든 파일 삭제
        }

        // 새 파일 추가 처리
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    saveFile(file, postEntity);
                }
            }
        }

        postRepository.save(postEntity);
        return PostEntity.toDto(postEntity);
    }


    /**
     * 게시물을 삭제하는 메서드
     *
     * @param id 삭제할 게시물의 ID
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);

        log.info("Deleted Post ID: {}", id);
    }

    /**
     * 게시물을 검색하는 메서드
     */
    public List<PostDto> searchPosts(String keyword) {
        List<PostEntity> postEntities = postRepository.searchPosts(keyword);

        return postEntities.stream()
                .map(PostEntity::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 파일을 저장하고 데이터베이스에 저장 경로 정보를 저장하는 메서드
     */
    private void saveFile(MultipartFile file, PostEntity postEntity) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = uuid + extension;

        // 파일 저장 경로 (절대 경로)
        Path filePath = Paths.get(fileUploadPath, fileName);

        // 파일을 지정된 경로에 저장
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 파일 상대 경로 생성 (웹에서 접근할 수 있는 경로)
        String relativeFilePath = fileAccessPath + fileName;

        // PostFile 엔티티 생성 및 저장
        PostFile postFile = PostFile.builder()
                .fileName(originalFileName)
                .filePath(relativeFilePath)  // 상대 경로를 DB에 저장
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .post(postEntity)
                .uuid(uuid)
                .build();

        fileRepository.save(postFile);
    }

    /**
     * 게시물에 연결된 파일 리스트를 반환하는 메서드
     */
    public List<PostFile> getFilesByPostId(Long postId) {
        return fileRepository.findByPostId(postId);
    }

    /**
     * 게시글 수정시 업로드된을 삭제
     */
    public void deleteFile(Long fileId) {
        fileRepository.deleteById(fileId);
        log.info("Deleted file ID: {}", fileId);
    }
}
