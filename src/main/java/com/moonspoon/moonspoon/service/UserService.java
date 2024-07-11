package com.moonspoon.moonspoon.service;
import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.dto.request.user.UserSignupRequest;

import com.moonspoon.moonspoon.dto.request.user.UserValidateNameRequest;


import com.moonspoon.moonspoon.dto.response.error.DuplicateErrorResponse;
import com.moonspoon.moonspoon.dto.response.user.UserResponse;

import com.moonspoon.moonspoon.exception.DuplicateUserException;
import com.moonspoon.moonspoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse signup(UserSignupRequest dto){
        isDuplicatedUsername(dto.getUsername());
        isDuplicatedName(dto.getName());
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);

        User user = UserSignupRequest.toEntity(dto);
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }


    public DuplicateErrorResponse isDuplicatedUsername(String username){
        if(userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("중복된 아이디가 존재합니다.");
        }
        DuplicateErrorResponse response = new DuplicateErrorResponse("OK", true);
        return response;
    }

    public DuplicateErrorResponse isDuplicatedName(String name){
        if(userRepository.existsByName(name)){
            throw new DuplicateUserException("중복된 이름이 존재합니다.");
        }
        DuplicateErrorResponse response = new DuplicateErrorResponse("OK", true);
        return response;
    }

    public void validateName(UserValidateNameRequest dto){
        isDuplicatedName(dto.getName());
    }




}
