package com.groofycode.GroofyCode.repository;


import com.groofycode.GroofyCode.model.Game.FriendMatchInvitation;
import com.groofycode.GroofyCode.model.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendMatchInvitationRepository extends JpaRepository<FriendMatchInvitation, Long> {
    @Query("SELECT i FROM FriendMatchInvitation i WHERE " +
            "(i.sender = :sender AND i.receiver = :receiver) OR " +
            "(i.sender = :receiver AND i.receiver = :sender)")
    List<FriendMatchInvitation> findBySenderAndReceiverOrReceiverAndSender(
            @Param("sender") UserModel sender,
            @Param("receiver") UserModel receiver);
}
