package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatIdOrderByCreatedAtDesc(Long chatId, Pageable pageable);

    Page<Message> findByChatId(Long chatId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.userModel.id != :userId AND m.chat.id = :chatId AND m.isRead = false")
    Integer countByChatIdAndUnread(@Param("userId") Long userId, @Param("chatId") Long chatId);

    @Query("SELECT m FROM Message m WHERE m.userModel.id != :userId AND m.chat.id = :chatId AND m.isRead = false")
    List<Message> findAllChatIdAndUnread(@Param("userId") Long userId, @Param("chatId") Long chatId);

}
