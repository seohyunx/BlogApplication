package com.shr.blog.repository;

import com.shr.blog.domain.Comment;
import com.shr.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByWriter(User writer);
}
