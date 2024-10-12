package com.shr.blog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

/**
 * 쿠키 관리 클래스
 * -> OAuth2 인증 플로우를 구현하며, 쿠키를 사용할 일이 생길 때 유틸리티로 사용
 */
@Slf4j
public class CookieUtil {
    // 요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response,
                                 String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if(name. equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 변환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화해 객체로 변환
    public static <T> Optional<T> deserialize(Cookie cookie, Class<T> cls) {
//        return cls.cast(
//                SerializationUtils.deserialize(
//                        Base64.getUrlDecoder().decode(cookie.getValue())
//                )
//        );
        // 쿠키의 값을 직렬화된 객체로 디코딩
        byte[] serializedData = Base64.getUrlDecoder().decode(cookie.getValue());

        try {
            // 역직렬화된 객체를 해당 클래스 타입으로 캐스팅
            Object deserializedObject = SerializationUtils.deserialize(serializedData);
            return Optional.of(cls.cast(deserializedObject));
        } catch (Exception e) {
            // 역직렬화 과정에서 오류가 발생하면 로그를 남기고 빈 Optional 반환
            log.error("Failed to deserialize cookie", e);
        }

        return Optional.empty();
    }
}