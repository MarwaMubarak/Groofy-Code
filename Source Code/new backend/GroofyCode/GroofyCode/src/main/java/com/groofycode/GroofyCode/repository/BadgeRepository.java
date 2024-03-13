package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.BadgeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeModel,Long> {

    Optional<BadgeModel> findByName(String name);
    Optional<BadgeModel> findByPhoto(String photo);
    Optional<BadgeModel> findByDescription(String description);



}
