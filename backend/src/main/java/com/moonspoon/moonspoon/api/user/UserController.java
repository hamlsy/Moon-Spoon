package com.moonspoon.moonspoon.api.user;

import com.moonspoon.moonspoon.api.user.dto.request.CheckNameRequest;
import com.moonspoon.moonspoon.api.user.dto.request.CheckUsernameRequest;
import com.moonspoon.moonspoon.api.user.dto.request.UserSignupRequest;

import com.moonspoon.moonspoon.api.error.DuplicateErrorResponse;
import com.moonspoon.moonspoon.api.user.dto.response.UserAdminRoleResponse;
import com.moonspoon.moonspoon.api.user.dto.response.UserProfileResponse;
import com.moonspoon.moonspoon.api.user.dto.response.UserResponse;
import com.moonspoon.moonspoon.core.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest dto){
        UserResponse response = userService.signup(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/checkUsername")
    public ResponseEntity<?> checkUsername(@Valid @RequestBody CheckUsernameRequest dto){
        DuplicateErrorResponse response = userService.isDuplicatedUsername(dto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/checkName")
    public ResponseEntity<?> checkName(@Valid @RequestBody CheckNameRequest dto){
        DuplicateErrorResponse response = userService.isDuplicatedName(dto.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getProfile")
    public ResponseEntity<UserProfileResponse> getUserProfile(){
        UserProfileResponse response = userService.getUserProfile();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<UserAdminRoleResponse> getAdminRole(){
        UserAdminRoleResponse response = userService.isAdmin();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
