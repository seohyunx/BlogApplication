package com.shr.blog.controller;

import com.shr.blog.domain.User;
import com.shr.blog.dto.CommentDto;
import com.shr.blog.dto.PostDto;
import com.shr.blog.service.CommentService;
import com.shr.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/blog/posts/{postId}/comments")
public class CommentViewController {

    private final CommentService commentService;
    private final PostService postService;

    /**
     * 특정 게시물과 댓글을 조회하는 페이지
     */
    @GetMapping
    public String viewPostWithComments(@PathVariable Long postId, Model model, Authentication authentication) {
        PostDto postDto = postService.getPostById(postId);
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);

        User user = (User) authentication.getPrincipal();

        model.addAttribute("post", postDto);
        model.addAttribute("comments", comments);
        model.addAttribute("currentUser", user.getNickname());

        return "post";
    }

    /**
     * 댓글 추가
     */
    @PostMapping
    public String addComment(@PathVariable Long postId, @RequestParam String content, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        CommentDto commentDto = new CommentDto();
        commentDto.setContent(content);

        commentService.createComment(postId, commentDto, user);

        log.info("commentDto.getId(): {}", commentDto.getId());
        log.info("commentDto.getContent(): {}", commentDto.getContent());
        log.info("commentDto.getWriterNickname(): {}", commentDto.getWriterNickname());

        return "redirect:/blog/" + postId;
    }

    /**
     * 댓글 수정
     */
    @PostMapping("/{commentId}/edit")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                @RequestParam String content, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.updateComment(commentId, content, user);
        return "redirect:/blog/" + postId;
    }

    /**
     * 댓글 삭제
     */
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        commentService.deleteComment(commentId, user);

        model.addAttribute("postId", postId);
        model.addAttribute("commentId", commentId);
        return "redirect:/blog/" + postId;
    }
}
