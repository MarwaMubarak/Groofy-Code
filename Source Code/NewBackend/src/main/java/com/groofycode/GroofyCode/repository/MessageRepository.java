package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatIdOrderByCreatedAtAsc(Long chatId, Pageable pageable);
    Page<Message> findByChatId(Long chatId, Pageable pageable);

}
