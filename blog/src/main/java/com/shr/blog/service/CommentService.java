package com.shr.blog.service;

import com.shr.blog.domain.Comment;
import com.shr.blog.domain.PostEntity;
import com.shr.blog.domain.User;
import com.shr.blog.dto.CommentDto;
import com.shr.blog.repository.CommentRepository;
import com.shr.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 생성
     */
    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto, User user){
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found, Post Id: " + postId));

        // 댓글 엔티티 생성 및 저장
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .writer(user)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);

        log.info("comment.getContent(): {}", savedComment.getContent());
        log.info("comment.getWriter().getNickname(): {}", savedComment.getWriter().getNickname());

        return Comment.toDto(savedComment);
    }

    // 댓글 리스트 가져올 때 사용
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(Comment::toDto)
                .collect(Collectors.toList());
    }

    // 댓글 1개를 가져올 때 사용
    public CommentDto getCommentById(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));

        if (!comment.getWriter().getNickname().equals(user.getNickname())) {
            throw new IllegalStateException("You can only delete your own comments.");
        }

        log.info("Comment Writer: {}, Logged-in User: {}", comment.getWriter().getNickname(), user.getNickname());

        return Comment.toDto(comment);
    }

    @Transactional
    public CommentDto updateComment(Long commentId, String content, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found, Comment Id: " + commentId));

        if (!comment.getWriter().getNickname().equals(user.getNickname())) {
            throw new IllegalStateException("You can only update your own comments.");
        }

        comment.updateContent(content);
        Comment updatedComment = commentRepository.save(comment);

        return Comment.toDto(updatedComment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found, Comment Id: " + commentId));

        if (!comment.getWriter().getNickname().equals(user.getNickname())) {
            throw new IllegalStateException("You can only delete your own comments.");
        }

        commentRepository.delete(comment);
        log.info("Deleted Comment ID: {}", commentId);
    }
}
