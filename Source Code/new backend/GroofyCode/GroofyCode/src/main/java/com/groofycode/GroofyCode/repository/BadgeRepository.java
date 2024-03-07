package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.BadgeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeModel,Long> {
}
