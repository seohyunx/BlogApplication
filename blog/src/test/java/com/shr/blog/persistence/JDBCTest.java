package com.shr.blog.persistence;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class JDBCTest {
    static {
        try {
            // JDBC 드라이버 로드
            Class.forName("org.h2.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("H2 Database 연결 성공")
    public void testConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:file:./data/blog-db", "sa", "");

            log.info("testConnection: {}", connection);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            // Connection 객체 해제
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
        }
    }
}
