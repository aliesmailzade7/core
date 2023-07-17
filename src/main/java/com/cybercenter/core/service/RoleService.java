package com.cybercenter.core.service;

import com.cybercenter.core.repository.RoleRepository;
import com.cybercenter.core.constant.BaseUserRole;
import com.cybercenter.core.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Method for create new role.
     * If the role does not exist in the repository, it creates it
     *
     * @param baseUserRole - BaseUserRole object
     */
    @Cacheable(key = "#baseUserRole.id")
    public Role createRoleIfNotFound(BaseUserRole baseUserRole) {
        Role role = roleRepository.findByName(baseUserRole.getName());
        if (ObjectUtils.isEmpty(role)) {
            Role newRole = new Role();
            newRole.setName(baseUserRole.getName());
            newRole.setAuthorities(new ArrayList<>());
            return roleRepository.save(newRole);
        }
        return role;
    }
}
