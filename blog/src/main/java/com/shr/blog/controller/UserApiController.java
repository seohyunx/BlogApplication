package com.shr.blog.controller;

import com.shr.blog.dto.UserDto;
import com.shr.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(UserDto userDto) {
        userService.save(userDto);
        return "redirect:/login";
    }
}