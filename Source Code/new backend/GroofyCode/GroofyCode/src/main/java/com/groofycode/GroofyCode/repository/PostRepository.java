package com.groofycode.GroofyCode.repository;
import com.groofycode.GroofyCode.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
    // You can define additional methods for custom queries if needed
}