package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.ClanModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClanRepository extends JpaRepository <ClanModel, Long> {
    ClanModel findByName(String name);
}
