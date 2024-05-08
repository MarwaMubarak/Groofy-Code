package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Post.LikeModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, Long> {
    LikeModel findByUserIdAndPostId(Long userId, Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM LikeModel l WHERE l.post.id=:postId")
    void deleteByPostId(@Param("postId") Long postId);
}