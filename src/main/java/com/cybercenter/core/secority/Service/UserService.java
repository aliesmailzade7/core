package com.cybercenter.core.secority.Service;

import com.cybercenter.core.secority.Entity.Role;
import com.cybercenter.core.secority.Repository.UserRepository;
import com.cybercenter.core.secority.constant.LoginMethodType;
import com.cybercenter.core.secority.constant.UserRole;
import com.cybercenter.core.secority.dto.UserDTO;
import com.cybercenter.core.secority.exception.EXPInvalidVerifyCode;
import com.cybercenter.core.secority.mapper.UserMapper;
import com.cybercenter.core.secority.Entity.User;
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

    public void save(UserDTO dto) {
        userRepository.save(userMapper.ToEntity(dto));
    }

    /**
     * Method for Assign the role to the user and save the user.
     *
     * @param dto    - UserDTO object
     * @param roles  - list of UserRole
     */
    public void save(UserDTO dto, List<UserRole> roles) {
        User user = userMapper.ToEntity(dto);
        List<Role> userRoles = new ArrayList<>();
        for (UserRole role : roles) {
            Role roleByName = roleService.createRoleIfNotFound(role);
            userRoles.add(roleByName);
        }
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    /**
     * Method for update the last login method type.
     *
     * @param username           - Username
     * @param loginMethodTypeId  - LoginMethodType object
     */
    public void updateLoginMethodType(String username, LoginMethodType loginMethodTypeId) {
        User user = (User) loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user) && (ObjectUtils.isEmpty(user.getLoginMethodType()) || !user.getLoginMethodType().equals(loginMethodTypeId))) {
            user.setLoginMethodType(loginMethodTypeId);
            userRepository.save(user);
        }
    }

    /**
     * Method for update the last login method type.
     *
     * @param user               - user object
     * @param loginMethodTypeId  - LoginMethodType object
     */
    public void updateLoginMethodType(User user, LoginMethodType loginMethodTypeId) {
        if (ObjectUtils.isEmpty(user.getLoginMethodType()) || !user.getLoginMethodType().equals(loginMethodTypeId)) {
            user.setLoginMethodType(loginMethodTypeId);
            userRepository.save(user);
        }
    }
}
