package com.groofycode.GroofyCode.repository;
import com.groofycode.GroofyCode.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
    List<PostModel> findByUserId(Long userId);
}