package com.shr.blog.controller;

import com.shr.blog.dto.UserDto;
import com.shr.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/api/blog")
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(UserDto userDto) {
        userService.createUser(userDto);
        return "redirect:/blog/login";
    }
}
