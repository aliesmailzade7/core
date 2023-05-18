package com.cybercenter.core.mesage.message.mapper;

import com.cybercenter.core.mesage.message.dto.MessageDTO;
import com.cybercenter.core.mesage.message.entity.MessageLog;
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
