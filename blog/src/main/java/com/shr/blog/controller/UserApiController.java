package com.shr.blog.controller;

import com.shr.blog.dto.UserDto;
import com.shr.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/blog")
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(UserDto userDto, Model model) {
        try {
            userService.createUser(userDto);
            return "redirect:/blog/login";  // 회원가입 성공 후 로그인 페이지로 리디렉션
        } catch (IllegalArgumentException ex) {
            // 예외 발생 시 오류 메시지를 모델에 추가
            model.addAttribute("errorMessage", ex.getMessage());
            return "signup";
        }
    }
}
