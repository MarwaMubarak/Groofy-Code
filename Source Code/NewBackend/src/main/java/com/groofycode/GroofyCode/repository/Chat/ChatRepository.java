package com.groofycode.GroofyCode.repository.Chat;

import com.groofycode.GroofyCode.model.Chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {
}
