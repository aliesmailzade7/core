package com.cybercenter.core.secority.Service;

import com.cybercenter.core.secority.Entity.Role;
import com.cybercenter.core.secority.Repository.RoleRepository;
import com.cybercenter.core.secority.constant.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role createRoleIfNotFound(UserRole userRole) {
        Role role = roleRepository.findByName(userRole.getName());
        if (ObjectUtils.isEmpty(role)) {
            Role newRole = new Role();
            newRole.setId(userRole.getId());
            newRole.setName(userRole.getName());
            newRole.setAuthorities(new ArrayList<>());
            return roleRepository.save(newRole);
        }
        return role;
    }
}
