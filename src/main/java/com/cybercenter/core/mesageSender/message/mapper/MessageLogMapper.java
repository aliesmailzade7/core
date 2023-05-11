package com.cybercenter.core.mesageSender.message.mapper;

import com.cybercenter.core.mesageSender.message.dto.MessageDTO;
import com.cybercenter.core.mesageSender.message.entity.MessageLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
@Component
public interface MessageLogMapper {

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    MessageLog toEntity(MessageDTO messageDTO);

}
