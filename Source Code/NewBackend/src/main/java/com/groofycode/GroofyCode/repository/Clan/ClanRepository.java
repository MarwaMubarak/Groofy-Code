package com.groofycode.GroofyCode.repository.Clan;

import com.groofycode.GroofyCode.model.Clan.ClanModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClanRepository extends JpaRepository<ClanModel, Long> {
    ClanModel findByNameIgnoreCase(String clanName);

    @Query("SELECT c FROM ClanModel c LEFT JOIN FETCH c.chat WHERE lower(c.name) = lower(:clanName)")
    ClanModel fetchByNameIgnoreCase(@Param("clanName") String clanName);

    @Query("SELECT COUNT(m) FROM ClanModel c JOIN c.members m WHERE c.id=:id")
    Integer countMembersById(Long id);

    @Query("SELECT COUNT(c) FROM ClanModel c")
    Integer countClans();

    @Query("SELECT COUNT(c) FROM ClanModel c WHERE c.name LIKE %:name%")
    Integer countSearchedClans(@Param("name") String name);

    Page<ClanModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
