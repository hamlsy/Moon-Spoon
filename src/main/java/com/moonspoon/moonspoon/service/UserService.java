package com.moonspoon.moonspoon.service;
import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.dto.request.UserSignupRequest;
import com.moonspoon.moonspoon.dto.response.UserResponse;
import com.moonspoon.moonspoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse join(UserSignupRequest dto){
        User user = UserSignupRequest.toEntity(dto);
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }
}
