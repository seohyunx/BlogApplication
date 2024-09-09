package com.shr.blog.service;

import com.shr.blog.domain.PostEntity;
import com.shr.blog.domain.User;
import com.shr.blog.dto.PostDto;
import com.shr.blog.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 비즈니스 로직을 처리하는 Service 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    /**
     * createPost: 게시물을 추가하는 메서드
     *
     * @param postDto 등록할 게시물의 정보
     * @return 생성된 PostEntity 객체
     */
    public PostDto createPost(PostDto postDto, User user) {
        log.info("Creating post with writer ID: {}", user.getId());

        PostEntity postEntity = postDto.toEntity(user);
        PostEntity savedPost = postRepository.save(postEntity);
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
    public PostDto updatePost(Long id, PostDto postDto, User user) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        PostEntity updatedPost = postEntity.update(postDto.getTitle(), postDto.getContent(), user);
        postRepository.save(updatedPost);

        return PostEntity.toDto(updatedPost);
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
}
