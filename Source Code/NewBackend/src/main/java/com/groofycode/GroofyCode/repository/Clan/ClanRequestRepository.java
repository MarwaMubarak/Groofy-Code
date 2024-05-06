package com.groofycode.GroofyCode.repository.Clan;

import com.groofycode.GroofyCode.model.Clan.ClanRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClanRequestRepository extends JpaRepository<ClanRequest, Long> {
    @Query("SELECT cr FROM ClanRequest cr WHERE cr.user.id=:userId AND cr.clan.id=:clanId")
    ClanRequest findByUserIdAndClanId(@Param("userId") Long userId, @Param("clanId") Long clanId);

    @Query("SELECT cr FROM ClanRequest cr JOIN FETCH cr.user WHERE cr.clan.id=:clanId")
    Page<ClanRequest> fetchByClanId(@Param("clanId") Long clanId, Pageable pageable);

    @Query("SELECT COUNT(cr) FROM ClanRequest cr WHERE cr.clan.id=:clanId")
    Integer countClanRequestByClanId(@Param("clanId") Long clanId);
}
