package com.shr.blog.controller;

import com.shr.blog.domain.PostFile;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/blog/posts")
public class PostViewController {

    private final PostService postService;
    private final UserService userService;

    /**
     * 모든 게시물 조회
     */
    @GetMapping()
    // Model 객체: Controller에서 생성한 데이터를 담아서 View로 전달할 때 사용하는 객체
    public String getAllPosts(Model model, Authentication authentication) {
        List<PostDto> board = postService.getAllPosts();
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
        PostDto postDto = postService.getPostById(id);
        model.addAttribute("post", postDto);

        log.info("Post ID: {}", postDto.getId());

        User user = (User) authentication.getPrincipal();
        model.addAttribute("currentUser", user);

        List<PostFile> files = postService.getFilesByPostId(id);
        model.addAttribute("files", files);

        return "post";
    }

    /**
     * 새 게시물 작성 및 수정 폼 표시
     * @param id 수정할 게시물의 ID (없으면 새 게시물 작성)
     */
    @GetMapping({"/write", "/{id}/edit"})
    public String showEditForm(@PathVariable(required = false) Long id, Model model, Authentication authentication) {
        if (id == null) {
            model.addAttribute("postDto", new PostDto());
        } else {
            PostDto postDto = postService.getPostById(id);
            model.addAttribute("postDto", postDto);

            List<PostFile> files = postService.getFilesByPostId(id);
            model.addAttribute("files", files);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);

        return "write";
    }

    /**
     * 게시물 생성 및 수정 처리
     */
    @PostMapping({"/write", "/edit"})
    public String createPost(@ModelAttribute PostDto postDto, Authentication authentication,
                             @RequestParam MultipartFile[] files,
                             @RequestParam(required = false) Long[] existingFileIds) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (postDto.getId() == null) {
            PostDto createdPost = postService.createPost(postDto, user, files);
            return "redirect:/blog/posts/" + createdPost.getId();
        } else {
            postService.updatePost(postDto.getId(), postDto, user, existingFileIds, files);
            return "redirect:/blog/posts/" + postDto.getId();
        }
    }


    /**
     * 게시물 삭제
     */
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);

        return "redirect:/blog/posts";
    }

    /**
     * 게시물 검색
     */
    @GetMapping("/search")
    public String searchPosts(@RequestParam("keyword") String keyword, Model model, Authentication authentication) {
        List<PostDto> searchResults = postService.searchPosts(keyword);
        model.addAttribute("board", searchResults);

        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);

        return "board";
    }
}
