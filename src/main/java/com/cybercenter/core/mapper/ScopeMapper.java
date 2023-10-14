package com.cybercenter.core.mapper;

import com.cybercenter.core.dto.ScopeDTO;
import com.cybercenter.core.entity.ScopeEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ScopeMapper {
    ScopeDTO toDTO(ScopeEntity entity);
    List<ScopeDTO> toDTOs(List<ScopeEntity> entity);
    ScopeEntity toEntity(ScopeDTO toDTO);
}
