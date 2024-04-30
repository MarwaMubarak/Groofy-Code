package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Post.LikeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, Long> {
    LikeModel findByUserIdAndPostId(Long userId, Long postId);
}