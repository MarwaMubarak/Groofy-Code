package com.groofycode.GroofyCode.repository;
import com.groofycode.GroofyCode.model.ClanModel;
import com.groofycode.GroofyCode.model.LikeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<LikeModel, Long> {

    List<LikeModel> findAllByPostId(Long postId);

}