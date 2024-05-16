package com.groofycode.GroofyCode.repository;
import com.groofycode.GroofyCode.model.Post.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
    List<PostModel> findByUserIdOrderByCreatedAtDesc(Long userId);
}