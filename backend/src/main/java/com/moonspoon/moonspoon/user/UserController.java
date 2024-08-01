package com.moonspoon.moonspoon.user;

import com.moonspoon.moonspoon.dto.request.user.CheckNameRequest;
import com.moonspoon.moonspoon.dto.request.user.CheckUsernameRequest;
import com.moonspoon.moonspoon.dto.request.user.UserSignupRequest;

import com.moonspoon.moonspoon.dto.request.user.UserValidateNameRequest;

import com.moonspoon.moonspoon.dto.response.error.DuplicateErrorResponse;
import com.moonspoon.moonspoon.dto.response.user.UserResponse;
import com.moonspoon.moonspoon.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest dto){
        UserResponse response = userService.signup(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/validateName")
    public ResponseEntity<?> validateName(@Valid @RequestBody UserValidateNameRequest dto){

        return new ResponseEntity<>(null, HttpStatus.OK);
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

}
