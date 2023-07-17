package com.cybercenter.core.service;

import com.cybercenter.core.entity.User;
import com.cybercenter.core.security.jwt.UserPrincipal;
import com.cybercenter.core.repository.UserRepository;
import com.cybercenter.core.exception.EXPNotFoundUserName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(EXPNotFoundUserName::new);
        return new UserPrincipal(user);
    }

}
