package com.shr.blog.service;

import com.shr.blog.domain.PostEntity;
import com.shr.blog.domain.User;
import com.shr.blog.dto.PostDto;
import com.shr.blog.repository.PostRepository;
import com.shr.blog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@AutoConfigureMockMvc
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("createPost: 게시물 생성 테스트")
    public void createPost() {
        // given
        User user = User.builder()
                .email("test@gmail.com")
                .password("test_password")
                .nickname("test_nickname")
                .build();

        PostDto postDto = PostDto.builder()
                .title("Test Title")
                .content("Test Content")
                .build();

        PostEntity savedPostEntity = PostEntity.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();

        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(postRepository.save(any(PostEntity.class))).thenReturn(savedPostEntity);

        // when
        PostDto result = postService.createPost(postDto, user);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(postDto.getTitle());
        assertThat(result.getContent()).isEqualTo(postDto.getContent());
        assertThat(result.getWriter()).isEqualTo(user.getNickname());
    }
}
