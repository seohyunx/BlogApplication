package com.shr.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class CommentApiControllerTest {

    @Test
    @DisplayName("addComment: 댓글 등록")
    void addComment() {
        // given

        // when

        // then
    }

    @Test
    void getCommentsByPost() {
    }

    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }
}