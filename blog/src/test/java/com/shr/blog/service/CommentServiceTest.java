package com.shr.blog.service;

import com.shr.blog.domain.Comment;
import com.shr.blog.domain.PostEntity;
import com.shr.blog.domain.User;
import com.shr.blog.dto.CommentDto;
import com.shr.blog.repository.CommentRepository;
import com.shr.blog.repository.PostRepository;
import com.shr.blog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User loginUser;

    @BeforeEach
    void setUpUser() {
        loginUser = User.builder()
                .email("test" + System.currentTimeMillis() + "@gmail.com")
                .password("test")
                .nickname("test_nickname")
                .build();
        userRepository.save(loginUser);
    }

    @Test
    @Transactional
    @DisplayName("addComment: 댓글 등록 성공")
    public void addCommentTest() {
        // given
//        User loginUser = User.builder()
//                .email("test5@gmail.com")
//                .password("test5")
//                .nickname("test5_nickname")
//                .build();
//
//        userRepository.save(loginUser);
//
//        log.info("loginUser: {}", loginUser.getNickname());

        CommentDto comment = CommentDto.builder()
                .content("Add Comment Test")
                .writerNickname("test5_nickname")
                .build();

        // when
        CommentDto savedComment = commentService.createComment(1L, comment, loginUser);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("Add Comment Test");
        assertThat(savedComment.getWriterNickname()).isEqualTo("test_nickname");
    }

    @Test
    @DisplayName("getCommentsByPost: 게시물에서 댓글 가져오기 성공")
    public void getCommentsByPostTest() {
        // given
        PostEntity post = PostEntity.builder()
                .title("Test Post")
                .content("Test Post Content")
                .writer(loginUser)
                .build();

        postRepository.save(post);

        Comment comment_1 = Comment.builder()
                .content("Test Comment_1")
                .writer(loginUser)
                .post(post)
                .build();

        Comment comment_2 = Comment.builder()
                .content("Test Comment_2")
                .writer(loginUser)
                .post(post)
                .build();

        commentRepository.save(comment_1);
        commentRepository.save(comment_2);

        // when
        List<Comment> comments = commentRepository.findByPostId(post.getId());

        // then
        assertThat(comments).isNotNull();
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments).extracting("content").contains("Test Comment_1");
        assertThat(comments).extracting("content").contains("Test Comment_2");
    }

    @Test
    @Transactional
    @DisplayName("updateComment: 댓글 업데이트 성공")
    void updateCommentTest() {
        // given
        // loginUser 를 저장소에 저장하여 영속성을 부여
        User savedUser = userRepository.save(loginUser);

        log.info("savedUser email: {}", savedUser.getEmail());

        PostEntity post = PostEntity.builder()
                .title("Test Post")
                .content("Test Post Content")
                .writer(savedUser)
                .build();

        postRepository.save(post);

        Comment comment = Comment.builder()
                .content("Test Comment_1")
                .writer(savedUser)
                .post(post)
                .build();

        commentRepository.save(comment);

        // 업데이트할 content 설정
        String updateContent = "Update comment content";

        // when
        CommentDto updatedComment = commentService.updateComment(comment.getId(), updateContent, savedUser);

        // then
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getContent()).isEqualTo(updateContent);
        assertThat(updatedComment.getWriterNickname()).isEqualTo(savedUser);
    }

    @Test
    @Transactional
    @DisplayName("deleteComment: 댓글 삭제 성공")
    void deleteCommentTest() {
        // given
        // loginUser 를 저장소에 저장하여 영속성을 부여
        User savedUser = userRepository.save(loginUser);

        PostEntity post = PostEntity.builder()
                .title("Test Post")
                .content("Test Post Content")
                .writer(savedUser)
                .build();

        postRepository.save(post);

        Comment comment_1 = Comment.builder()
                .content("Test Comment_1")
                .writer(savedUser)
                .post(post)
                .build();

        Comment comment_2 = Comment.builder()
                .content("Test Comment_2")
                .writer(savedUser)
                .post(post)
                .build();

        commentRepository.save(comment_1);
        commentRepository.save(comment_2);

        // when
        commentService.deleteComment(comment_1.getId(), savedUser);

        // then
        assertThat(commentRepository.findById(comment_1.getId())).isEmpty();
        assertThat(commentRepository.findById(comment_2.getId())).isPresent();
    }
}