package com.cybercenter.core.user.service;

import com.cybercenter.core.auth.dto.RegisterRequestDTO;
import com.cybercenter.core.user.dto.UserInfoDTO;
import com.cybercenter.core.user.entity.Role;
import com.cybercenter.core.user.entity.User;
import com.cybercenter.core.user.exception.EXPNotFoundUserName;
import com.cybercenter.core.user.repository.UserRepository;
import com.cybercenter.core.auth.constant.LoginMethodType;
import com.cybercenter.core.user.constant.BaseUserRole;
import com.cybercenter.core.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

    public User findByUserName(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Method for Assign the role to the user and save the user.
     *
     * @param dto   - UserDTO object
     * @param roles - list of BaseUserRole
     */
    public void save(RegisterRequestDTO dto, List<BaseUserRole> roles) {
        User user = userMapper.ToEntity(dto);
        List<Role> userRoles = new ArrayList<>();
        for (BaseUserRole role : roles) {
            Role roleByName = roleService.createRoleIfNotFound(role);
            userRoles.add(roleByName);
        }
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    /**
     * Method for update the last login method type.
     *
     * @param username          - Username
     * @param loginMethodTypeId - LoginMethodType object
     */
    public void updateLoginMethodType(String username, LoginMethodType loginMethodTypeId) {
        User user = findByUserName(username);
        if (!ObjectUtils.isEmpty(user) && (ObjectUtils.isEmpty(user.getLoginMethodType()) || !user.getLoginMethodType().equals(loginMethodTypeId))) {
            user.setLoginMethodType(loginMethodTypeId);
            userRepository.save(user);
        }
    }

    /**
     * Method for update the last login method type.
     *
     * @param user              - user object
     * @param loginMethodTypeId - LoginMethodType object
     */
    public void updateLoginMethodType(User user, LoginMethodType loginMethodTypeId) {
        if (ObjectUtils.isEmpty(user.getLoginMethodType()) || !user.getLoginMethodType().equals(loginMethodTypeId)) {
            user.setLoginMethodType(loginMethodTypeId);
            userRepository.save(user);
        }
    }

    /**
     * Method for get the user info.
     *
     * @param userName              - username
     * @throws  EXPNotFoundUserName - not found user
     *
     * @return UserInfoDTO - UserInfoDTO object
     */
    public UserInfoDTO getUserProfile(String userName) {
        User user = findByUserName(userName);
        if (!ObjectUtils.isEmpty(user))
            return userMapper.toDTO(user);
        else
            throw new EXPNotFoundUserName();
    }

    public void updateProfile(UserInfoDTO userInfoDTO, String username) {
        User user = findByUserName(username);
        if (!ObjectUtils.isEmpty(user)) {
            userMapper.updateUserInfo(user, userInfoDTO);
            userRepository.save(user);
        }else
            throw new EXPNotFoundUserName();
    }
}
