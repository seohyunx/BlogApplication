package com.shr.blog.controller;

import com.shr.blog.domain.User;
import com.shr.blog.dto.CommentDto;
import com.shr.blog.service.CommentService;
import com.shr.blog.service.PostService;
import com.shr.blog.service.UserService;
import com.shr.blog.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentApiController {

    private final CommentService commentService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto, Authentication authentication) {
        User user = authenticationUtil.getAuthenticatedUser(authentication);
        CommentDto createdComment = commentService.createComment(postId, commentDto, user);

        return ResponseEntity.ok(createdComment);
    }

    /**
     * 특정 게시물의 댓글 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);

        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto, Authentication authentication) {
        User user = authenticationUtil.getAuthenticatedUser(authentication);
        CommentDto updatedComment = commentService.updateComment(commentId, commentDto.getContent(), user);

        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        User user = authenticationUtil.getAuthenticatedUser(authentication);
        commentService.deleteComment(commentId, user);

        return ResponseEntity.noContent().build();
    }
}