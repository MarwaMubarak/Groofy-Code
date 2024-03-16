package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.ClanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClanRepository extends JpaRepository <ClanModel, Long> {
    Optional<ClanModel> findByName(String name);
}
