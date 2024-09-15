package com.shr.blog.repository;

import com.shr.blog.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<PostFile, Long> {
    List<PostFile> findByPostId(Long postId);
}
