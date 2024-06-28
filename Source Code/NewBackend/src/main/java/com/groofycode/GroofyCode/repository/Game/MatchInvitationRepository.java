package com.groofycode.GroofyCode.repository.Game;
import com.groofycode.GroofyCode.model.Game.Game;
import com.groofycode.GroofyCode.model.Game.MatchInvitation;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchInvitationRepository extends JpaRepository<MatchInvitation, Long> {
    Optional<MatchInvitation> findByGameAndReceiver(Game game, UserModel receiver);
}
