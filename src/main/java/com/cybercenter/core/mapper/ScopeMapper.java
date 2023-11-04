package com.cybercenter.core.mapper;

import com.cybercenter.core.dto.ScopeDTO;
import com.cybercenter.core.entity.ScopeEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScopeMapper {
    ScopeDTO toDTO(ScopeEntity entity);

    List<ScopeDTO> toDTOs(List<ScopeEntity> entity);

    ScopeEntity toEntity(ScopeDTO toDTO);
}
