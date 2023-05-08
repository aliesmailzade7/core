package com.sybercenter.core.mesageSender.message.repository;

import com.sybercenter.core.mesageSender.message.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}
