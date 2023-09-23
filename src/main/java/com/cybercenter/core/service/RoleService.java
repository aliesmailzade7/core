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
import java.util.List;

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
//    @Cacheable(key = "#role.id")
    public Role createRoleIfNotFound(BaseUserRole baseUserRole) {
        Role role = roleRepository.findByTitle(baseUserRole.getTitle());
        if (ObjectUtils.isEmpty(role)) {
            Role newRole = new Role();
            newRole.setTitle(baseUserRole.getTitle());
            newRole.setAuthorities(new ArrayList<>());
            return roleRepository.save(newRole);
        }
        return role;
    }

    /**
     * Method for get role list.
     *
     * @return list<Role>
     */
    @Cacheable(key = "'list'")
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * Method for get role by id.
     *
     * @return list<Role>
     */
    @Cacheable
    public Role findById(int id) {
        return roleRepository.findById(id).orElse(null);
    }
}
