package com.groofycode.GroofyCode.repository;

import com.groofycode.GroofyCode.model.Chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
