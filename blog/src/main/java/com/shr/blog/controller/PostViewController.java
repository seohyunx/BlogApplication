package com.shr.blog.controller;

import com.shr.blog.domain.PostEntity;
import com.shr.blog.domain.User;
import com.shr.blog.dto.PostDto;
import com.shr.blog.service.PostService;
import com.shr.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/blog")
public class PostViewController {

    private final PostService postService;
    private final UserService userService;

    /**
     * 모든 게시물 조회
     */
    @GetMapping("")
    // Model 객체: Controller에서 생성한 데이터를 담아서 View로 전달할 때 사용하는 객체
    public String getAllPosts(Model model, Authentication authentication) {
        List<PostDto> board = postService.getAllPosts().stream()
                .map(PostEntity::toDto)
                .collect(Collectors.toList());
        model.addAttribute("board", board);

        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);

        return "board";
    }

    /**
     * 특정 ID의 게시물 조회
     */
    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model, Authentication authentication) {
        PostEntity postEntity = postService.getPostById(id);
        model.addAttribute("post", PostEntity.toDto(postEntity));

        log.info("Post ID: {}", PostEntity.toDto(postEntity).getId());

        User user = (User) authentication.getPrincipal();
        model.addAttribute("currentUser", user);

        return "post";
    }


    /**
     * 새 게시물 작성/수정 폼을 보여주는 메서드
     * @param id 수정할 게시물의 ID (없으면 새 게시물 작성)
     */
    @GetMapping({"/write", "/edit/{id}"})
    public String showEditForm(@PathVariable(required = false) Long id, Model model, Authentication authentication) {
        if (id == null) {
            model.addAttribute("postDto", new PostDto());
        } else {
            PostEntity postEntity = postService.getPostById(id);
            model.addAttribute("postDto", PostEntity.toDto(postEntity));
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);

        return "write";
    }

    /**
     * 게시물 생성/수정 처리
     */
    @PostMapping({"/write", "/edit"})
    public String savePost(@ModelAttribute PostDto postDto, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 로그 추가: user가 올바르게 전달되고 있는지 확인
        log.info("Saving post with user ID: {}", user.getId());

        if (postDto.getId() == null) {
            PostEntity createdPost = postService.createPost(postDto, user);
            return "redirect:/blog/" + createdPost.getId();
        } else {
            postService.updatePost(postDto.getId(), postDto, user);
            return "redirect:/blog/" + postDto.getId();
        }
    }


    /**
     * 게시물 삭제
     */
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/blog";
    }
}
