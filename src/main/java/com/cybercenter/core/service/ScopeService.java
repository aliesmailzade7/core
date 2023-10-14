package com.cybercenter.core.service;

import com.cybercenter.core.dto.ScopeDTO;
import com.cybercenter.core.mapper.ScopeMapper;
import com.cybercenter.core.service.cache.ScopeCacheService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScopeService {

    ScopeCacheService scopeCacheService;
    ScopeMapper scopeMapper;

    public List<ScopeDTO> findAll() {
        return scopeMapper.toDTOs(scopeCacheService.findAll());
    }

    public ScopeDTO findById(int scopeId) {
        return scopeMapper.toDTO(scopeCacheService.findById(scopeId));
    }

    public ScopeDTO save(ScopeDTO dto) {
        return scopeMapper.toDTO(scopeCacheService.save(scopeMapper.toEntity(dto)));
    }

    public ScopeDTO update(int scopeId, ScopeDTO dto) {
        dto.setId(scopeId);
        return scopeMapper.toDTO(scopeCacheService.save(scopeMapper.toEntity(dto)));
    }
}
