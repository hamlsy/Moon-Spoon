package com.moonspoon.moonspoon.security;

import com.moonspoon.moonspoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import com.moonspoon.moonspoon.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        User user = userRepository.findByUsername(username);

        if (user != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(user);
        }

        return null;
    }
}