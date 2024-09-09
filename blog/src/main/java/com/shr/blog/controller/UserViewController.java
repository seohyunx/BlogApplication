package com.shr.blog.controller;

import com.shr.blog.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/blog")
@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 회원가입 폼 페이지로 이동
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signup";
    }
}
