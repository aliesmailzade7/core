package com.cybercenter.core.mesageSender.message.repository;

import com.cybercenter.core.mesageSender.message.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}
