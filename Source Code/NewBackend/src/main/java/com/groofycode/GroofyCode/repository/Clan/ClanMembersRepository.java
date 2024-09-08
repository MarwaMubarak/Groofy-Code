package com.groofycode.GroofyCode.repository.Clan;

import com.groofycode.GroofyCode.model.Clan.ClanMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ClanMembersRepository extends JpaRepository<ClanMember, Long> {
    @Query("SELECT c FROM ClanMember c WHERE c.clan.id=:clanId")
    Page<ClanMember> findByClanId(Long clanId, Pageable pageable);
}
