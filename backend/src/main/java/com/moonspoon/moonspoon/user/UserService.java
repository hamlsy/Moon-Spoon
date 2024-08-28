package com.moonspoon.moonspoon.user;
import com.moonspoon.moonspoon.dto.request.user.UserSignupRequest;

import com.moonspoon.moonspoon.dto.request.user.UserValidateNameRequest;


import com.moonspoon.moonspoon.dto.response.error.DuplicateErrorResponse;
import com.moonspoon.moonspoon.dto.response.user.UserAdminRoleResponse;
import com.moonspoon.moonspoon.dto.response.user.UserProfileResponse;
import com.moonspoon.moonspoon.dto.response.user.UserResponse;

import com.moonspoon.moonspoon.exception.DuplicateUserException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbookRepository;
import com.moonspoon.moonspoon.test.TestRepository;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.workbook.WorkbookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WorkbookRepository workbookRepository;
    private final SharedWorkbookRepository sharedWorkbookRepository;
    private final TestRepository testRepository;

    public UserResponse signup(UserSignupRequest dto){
        isDuplicatedUsername(dto.getUsername());
        isDuplicatedName(dto.getName());
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);

        User user = UserSignupRequest.toEntity(dto);
        user.setSignupDate(LocalDateTime.now());
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

    public UserProfileResponse getUserProfile(){
        User user = getCurrentUser();
        UserProfileResponse countResponse = userRepository.userProfileCountById(user.getId());
        UserProfileResponse response = setUserProfile(user, countResponse);

        if(user.getRole().getValue().equals("user")){
            response.setRole("일반 회원");
        }else{
            response.setRole("관리자");
        }
        return response;
    }

    private UserProfileResponse setUserProfile(User user, UserProfileResponse response){
        response.setName(user.getName());
        response.setWorkbookTestCount(user.getWorkbookTestCount());
        response.setUsername(user.getUsername());
        response.setSignupDate(user.getSignupDate());
        return response;
    }

    public UserAdminRoleResponse isAdmin(){
        User user = getCurrentUser();
        UserAdminRoleResponse response = new UserAdminRoleResponse();
        if(user.getRole().getValue().equals("admin")){
            response.setAdmin(true);
        }
        else{
            response.setAdmin(false);
        }
        return response;
    }

    private User getCurrentUser(){
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new NotUserException("존재하지 않는 유저입니다.");
        }
        return user;
    }

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
