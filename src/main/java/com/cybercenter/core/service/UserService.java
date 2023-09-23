package com.cybercenter.core.service;

import com.cybercenter.core.constant.BaseUserRole;
import com.cybercenter.core.constant.VerifyCodeType;
import com.cybercenter.core.dto.RegisterRequestDTO;
import com.cybercenter.core.dto.UserInfoDTO;
import com.cybercenter.core.dto.VerificationDTO;
import com.cybercenter.core.entity.Role;
import com.cybercenter.core.entity.User;
import com.cybercenter.core.exception.*;
import com.cybercenter.core.mapper.UserMapper;
import com.cybercenter.core.otp.VerificationUtils;
import com.cybercenter.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final VerificationUtils verificationUtils;
    private final PasswordEncoder passwordEncoder;

    public User findByUserName(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findById(long userId) {
        return userRepository.findById(userId).orElseThrow(EXPNotFoundUserInfo::new);
    }

    public Page<UserInfoDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
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
     * Method for get the user info.
     *
     * @param userName - username
     * @return UserInfoDTO - UserInfoDTO object
     * @throws EXPNotFoundUserName - not found user
     */
    public UserInfoDTO getUserProfile(String userName) {
        User user = findByUserName(userName);
        if (!ObjectUtils.isEmpty(user))
            return userMapper.toDTO(user);
        else
            throw new EXPNotFoundUserName();
    }

    public void updateProfile(long userId, UserInfoDTO userInfoDTO) {
        User user = findById(userId);
        checkChangePhoneNumber(user, userInfoDTO);
        checkChangeEmail(user, userInfoDTO);
        userMapper.updateUserInfo(user, userInfoDTO);
        userRepository.save(user);
    }

    private void checkChangeEmail(User user, UserInfoDTO userInfoDTO) {
        if (!ObjectUtils.isEmpty(userInfoDTO.getEmail()) && !userInfoDTO.getEmail().equals(user.getEmail())) {
            checkIsExistEmail(userInfoDTO.getEmail());
            user.setEmail(user.getEmail());
            if (!ObjectUtils.isEmpty(userInfoDTO.getVerifyEmailCode())) {
                boolean validateVerifyCode = verificationUtils.validateVerifyCode(userInfoDTO.getEmail(), userInfoDTO.getVerifyEmailCode(), VerifyCodeType.VERIFY_EMAIL);
                if (validateVerifyCode)
                    user.setVerifyEmail(true);
                else
                    throw new EXPInvalidVerifyCode();
            }
        }
    }

    private void checkIsExistEmail(String email) {
        User user = userRepository.findFirstByEmail(email);
        if (ObjectUtils.isEmpty(user))
            throw new EXPEmailAddressIsExist();
    }

    private void checkChangePhoneNumber(User user, UserInfoDTO userInfoDTO) {
        if (!ObjectUtils.isEmpty(userInfoDTO.getPhoneNumber()) && !userInfoDTO.getPhoneNumber().equals(user.getPhoneNumber())) {
            checkIsExistPhoneNumber(userInfoDTO.getPhoneNumber());
            user.setPhoneNumber(user.getPhoneNumber());
            if (!ObjectUtils.isEmpty(userInfoDTO.getVerifyPhoneNumberCode())) {
                boolean validateVerifyCode = verificationUtils.validateVerifyCode(userInfoDTO.getPhoneNumber(), userInfoDTO.getVerifyPhoneNumberCode(), VerifyCodeType.VERIFY_PHONE_NUMBER);
                if (validateVerifyCode)
                    user.setVerifyPhoneNumber(true);
                else
                    throw new EXPInvalidVerifyCode();
            }
        }
    }

    private void checkIsExistPhoneNumber(String phoneNumber) {
        User user = userRepository.findFirstByPhoneNumber(phoneNumber);
        if (ObjectUtils.isEmpty(user))
            throw new EXPPhoneNumberIsExist();
    }


    public void verifyUserPhoneNumberOrEmail(long userId, VerificationDTO dto, VerifyCodeType verifyCodeType) {
        User user = userRepository.findById(userId).orElse(null);
        if (ObjectUtils.isEmpty(user))
            return;

        boolean validateVerifyCode;
        switch (verifyCodeType) {
            case VERIFY_PHONE_NUMBER -> {
                if (!ObjectUtils.isEmpty(user.getPhoneNumber())) {
                    validateVerifyCode = verificationUtils.validateVerifyCode(user.getPhoneNumber(), dto.getVerifyCode(), VerifyCodeType.VERIFY_PHONE_NUMBER);
                    if (validateVerifyCode) {
                        user.setVerifyPhoneNumber(true);
                        save(user);
                    } else
                        throw new EXPInvalidVerifyCode();
                }
            }
            case VERIFY_EMAIL -> {
                if (!ObjectUtils.isEmpty(user.getEmail())) {
                    validateVerifyCode = verificationUtils.validateVerifyCode(user.getEmail(), dto.getVerifyCode(), VerifyCodeType.VERIFY_EMAIL);
                    if (validateVerifyCode) {
                        user.setVerifyEmail(true);
                        save(user);
                    } else
                        throw new EXPInvalidVerifyCode();
                }
            }
        }
    }

    public void blockAndUnblockUser(long userId) {
        User user = findById(userId);
        user.setEnable(!user.isEnable());
        userRepository.save(user);
    }

    public void setNewPassword(long userId, String newPassword) {
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void addRole(long userId, List<Integer> roleIds) {
        User user = findById(userId);
        List<Role> roles = roleService.findAll().stream()
                .filter(role -> roleIds.contains(role.getId()))
                .toList();
        user.setRoles(roles);
        save(user);
    }
}
