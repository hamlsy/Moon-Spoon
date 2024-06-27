package com.moonspoon.moonspoon.service;
import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.dto.request.UserSignupRequest;
import com.moonspoon.moonspoon.dto.response.UserResponse;
import com.moonspoon.moonspoon.exception.DuplicateUserException;
import com.moonspoon.moonspoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse signup(UserSignupRequest dto){
        isDuplicated(dto.getUsername(), dto.getName());
        User user = UserSignupRequest.toEntity(dto);
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    private void isDuplicated(String username, String name){
        if(userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("중복된 아이디가 존재합니다.");
        }
        else if(userRepository.existsByName(name)){
            throw new DuplicateUserException("중복된 이름이 존재합니다.");
        }
    }
}
