//package com.shr.blog.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.shr.blog.config.jwt.JwtFactory;
//import com.shr.blog.config.jwt.JwtProperties;
//import com.shr.blog.domain.RefreshToken;
//import com.shr.blog.domain.User;
//import com.shr.blog.dto.CreateAccessTokenDto;
//import com.shr.blog.repository.RefreshTokenRepository;
//import com.shr.blog.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class TokenApiControllerTest {
//
//    @Autowired
//    protected MockMvc mockMvc;
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    JwtProperties jwtProperties;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//    @BeforeEach
//    public void mockMvcSetUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .build();
//        //userRepository.deleteAll();
//    }
//
//    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
//    @Test
//    public void createNewAccessToken() throws Exception {
//        // given
//        final String url = "/api/token";
//
//        User testUser = userRepository.save(User.builder()
//                .email("user1@gmail.com")
//                .password("test1")
//                .nickname("test1_nickname")
//                .build());
//
//        String refreshToekn = JwtFactory.builder()
//                .claims(Map.of("id", testUser.getId()))
//                .build()
//                .createToken(jwtProperties);
//
//        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToekn));
//
//        CreateAccessTokenDto request = new CreateAccessTokenDto();
//        request.setRefreshToken(refreshToekn);
//        final String requestBody = objectMapper.writeValueAsString(request);
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(requestBody));
//
//        // then
//        resultActions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty());
//    }
//}