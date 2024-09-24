package com.moonspoon.moonspoon.core.user;
import com.moonspoon.moonspoon.api.user.dto.request.UserSignupRequest;
import com.moonspoon.moonspoon.api.error.DuplicateErrorResponse;
import com.moonspoon.moonspoon.api.user.dto.response.UserAdminRoleResponse;
import com.moonspoon.moonspoon.api.user.dto.response.UserProfileResponse;
import com.moonspoon.moonspoon.api.user.dto.response.UserResponse;

import com.moonspoon.moonspoon.common.exception.custom.DuplicateUserException;
import com.moonspoon.moonspoon.common.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 유저입니다.";
    private static final String DUPLICATED_USERNAME_MESSAGE = "중복된 아이디가 존재합니다.";
    private static final String DUPLICATED_NAME_MESSAGE = "중복된 이름이 존재합니다.";
    private static final String USER_ROLE = "일반 회원";
    private static final String ADMIN_ROLE = "관리자";
    private static final String USER_STRING = "user";
    private static final String ADMIN_STRING = "admin";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse signup(UserSignupRequest dto){
        isDuplicatedUsername(dto.getUsername());
        isDuplicatedName(dto.getName());

        User user = createUserEntity(dto);

        return UserResponse.fromEntity(userRepository.save(user));
    }

    private User createUserEntity(UserSignupRequest dto){
        User user = UserSignupRequest.toEntity(dto);
        user.setSignupDate(LocalDateTime.now());

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encodedPassword);

        return user;
    }

    public DuplicateErrorResponse isDuplicatedUsername(String username){
        if(userRepository.existsByUsername(username)) {
            throw new DuplicateUserException(DUPLICATED_USERNAME_MESSAGE);
        }
        DuplicateErrorResponse response = new DuplicateErrorResponse("OK", true);
        return response;
    }

    public DuplicateErrorResponse isDuplicatedName(String name){
        if(userRepository.existsByName(name)){
            throw new DuplicateUserException(DUPLICATED_NAME_MESSAGE);
        }
        DuplicateErrorResponse response = new DuplicateErrorResponse("OK", true);
        return response;
    }

    public UserProfileResponse getUserProfile(){
        User user = getAuthenticatedUser();
        UserProfileResponse countResponse = userRepository.userProfileCountById(user.getId());
        UserProfileResponse response = setUserProfile(user, countResponse);
        return response;
    }



    private UserProfileResponse setUserProfile(User user, UserProfileResponse response){
        response.setName(user.getName());
        response.setWorkbookTestCount(user.getWorkbookTestCount());
        response.setUsername(user.getUsername());
        response.setSignupDate(user.getSignupDate());
        response.setRole(getUserRoleString(user));
        return response;
    }

    private String getUserRoleString(User user){
        String currentUserRole = user.getRole().getValue();
        return currentUserRole.equals(USER_STRING) ? USER_ROLE : ADMIN_ROLE;
    }

    public UserAdminRoleResponse isAdmin(){
        User user = getAuthenticatedUser();
        UserAdminRoleResponse response = getUserAdminRoleResponse(user);
        return response;
    }

    private UserAdminRoleResponse getUserAdminRoleResponse(User user){
        UserAdminRoleResponse response = new UserAdminRoleResponse();
        response.setAdmin(
                user.getRole().getValue().equals(ADMIN_STRING) ? true : false
        );
        return response;
    }

    private User getAuthenticatedUser(){
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new NotFoundException(USER_NOT_FOUND_MESSAGE)
                );
        return user;
    }

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
