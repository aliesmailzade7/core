package com.sybercenter.core.secority.Service;

import com.sybercenter.core.secority.Repository.UserRepository;
import com.sybercenter.core.secority.dto.UserDTO;
import com.sybercenter.core.secority.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
}
