package com.groofycode.GroofyCode.repository.Game;

import com.groofycode.GroofyCode.model.Game.BeatAFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeatAFriendRepository extends JpaRepository<BeatAFriend, Long> {
    // You can add custom query methods here if needed
}
