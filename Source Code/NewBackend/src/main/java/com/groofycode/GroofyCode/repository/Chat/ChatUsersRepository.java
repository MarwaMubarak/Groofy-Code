package com.groofycode.GroofyCode.repository.Chat;

import com.groofycode.GroofyCode.model.Chat.ChatUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatUsersRepository extends JpaRepository<ChatUsers, Long> {
    @Query("SELECT cu FROM ChatUsers cu WHERE (cu.firstUser.id = :firstUserId AND cu.secondUser.id = :secondUserId) " +
            "OR (cu.firstUser.id = :secondUserId AND cu.secondUser.id = :firstUserId)")
    ChatUsers findByFirstUserIdAndSecondUserId(Long firstUserId, Long secondUserId);
}
