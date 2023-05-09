package com.sybercenter.core.secority.Service;

import com.sybercenter.core.secority.Entity.Role;
import com.sybercenter.core.secority.Entity.User;
import com.sybercenter.core.secority.Repository.UserRepository;
import com.sybercenter.core.secority.constant.LoginMethodType;
import com.sybercenter.core.secority.constant.UserRole;
import com.sybercenter.core.secority.dto.UserDTO;
import com.sybercenter.core.secority.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElse(null);
    }

    public UserDTO findByUserName(String username) {
        return userMapper.toDTO(userRepository.findByUsername(username).orElse(null));
    }

    public void setUser(UserDTO dto) {
        userRepository.save(userMapper.ToEntity(dto));
    }

    public void setUserByRole(UserDTO dto, List<UserRole> roles) {
        User user = userMapper.ToEntity(dto);
        List<Role> userRoles = new ArrayList<>();
        for (UserRole role : roles) {
            Role roleByName = roleService.createRoleIfNotFound(role);
            userRoles.add(roleByName);
        }
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    public void updateLoginMethodType(String username, LoginMethodType loginMethodTypeId) {
        User user = (User) loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user) && (ObjectUtils.isEmpty(user.getLoginMethodType()) || !user.getLoginMethodType().equals(loginMethodTypeId))) {
            user.setLoginMethodType(loginMethodTypeId);
            userRepository.save(user);
        }
    }
}
