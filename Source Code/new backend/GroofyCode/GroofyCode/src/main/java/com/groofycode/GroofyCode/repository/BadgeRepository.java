package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.BadgeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<BadgeModel,Long> {
}
