package com.groofycode.GroofyCode.repository;
import com.groofycode.GroofyCode.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
    // You can define additional methods for custom queries if needed
    List<PostModel> findByUserId(Long userId);
}