package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Optional<Chat> findByName(String name);

    @Query("SELECT c FROM Chat c JOIN c.participants p1 JOIN c.participants p2 WHERE p1.id = :userId1 AND p2.id = :userId2")
    Optional<Chat> findChatByUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
