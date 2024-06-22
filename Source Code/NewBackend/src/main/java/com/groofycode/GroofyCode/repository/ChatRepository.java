package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Optional<Chat> findByName(String name);

}
