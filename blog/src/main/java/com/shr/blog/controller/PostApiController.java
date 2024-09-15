package com.shr.blog.controller;

import com.shr.blog.domain.User;
import com.shr.blog.dto.PostDto;
import com.shr.blog.dto.UserDto;
import com.shr.blog.service.PostService;
import com.shr.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST API 컨트롤러로서, 게시물 관련 요청을 처리
 */
@Slf4j
@RequiredArgsConstructor        // final이나 @NonNull 필드에 대한 생성자를 생성
@RestController                 // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
@RequestMapping("/api/posts")    // 기본 URL 패턴을 /blog로 설정
public class PostApiController {

    private final PostService postService;
    private final UserService userService;

    /**
     * createPost: 새로운 게시물을 생성
     *
     * @param postDto 생성할 게시물 정보를 담은 DTO
     * @return 생성된 게시물 정보를 담은 ResponseEntity<PostDto>
     */
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, Authentication authentication, MultipartFile[] files) throws IOException {
        User user  = (User) authentication.getPrincipal();
        User userName = userService.findByEmail(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("postDto: {}", postDto.getContent());
        log.info("authentication: {}", ((UserDetails) authentication.getPrincipal()).getUsername());

        PostDto createdPost = postService.createPost(postDto, userName, files);
        return ResponseEntity.ok(createdPost);
    }

    /**
     * getAllPosts: 모든 게시물 조회
     *
     * @return 게시물 목록을 담은 List<PostDto>
     */
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> getBoardList = postService.getAllPosts();

        return ResponseEntity.ok(getBoardList);
    }

    /**
     * getPostById: 특정 ID를 가진 게시물을 조회
     *
     * @param id 조회할 게시물의 ID
     * @return 조회한 게시물 정보를 담은 ResponseEntity<PostDto>
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto postDto = postService.getPostById(id);

        return ResponseEntity.ok(postDto);
    }

    /**
     * updatePost: 기존 게시물을 수정
     *
     * @param id 수정할 게시물의 ID
     * @param postDto 수정할 게시물 정보를 담은 DTO
     * @return 수정된 게시물 정보를 담은 ResponseEntity<PostDto>
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto, User user, Long[] existingFileIds, MultipartFile[] files) throws IOException {
        log.info("Update request ID: {}", id);
        log.info("nickname: {}", user.getNickname());
        PostDto updatedPost = postService.updatePost(id, postDto, user, existingFileIds, files);

        return ResponseEntity.ok(updatedPost);
    }

    /**
     * deletePost: 특정 ID를 가진 게시물을 삭제
     *
     * @param id 삭제할 게시물의 ID
     * @return No Content 상태의 ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 게시물 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam("keyword") String keyword) {
        List<PostDto> searchResults = postService.searchPosts(keyword);

        return ResponseEntity.ok(searchResults);
    }
}