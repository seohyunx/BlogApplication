package com.shr.blog.repository;

import com.shr.blog.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JpaRepository를 상속받아 데이터베이스 조작을 위한 CRUD 인터페이스 구현
 */
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByOrderByIdDesc();               // ID를 내림차순으로 가져오기
}
