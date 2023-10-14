package com.cybercenter.core.service.cache;

import com.cybercenter.core.entity.ScopeEntity;
import com.cybercenter.core.repository.ScopeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CacheConfig(cacheNames = {"خrganization"})
public class ScopeCacheService {

    ScopeRepository scopeRepository;

    public List<ScopeEntity> findAll() {
        return scopeRepository.findAll();
    }

    public ScopeEntity findById(int groupId) {
        return scopeRepository.findById(groupId).orElse(null);
    }

    public ScopeEntity save(ScopeEntity entity) {
        return scopeRepository.save(entity);
    }
}
