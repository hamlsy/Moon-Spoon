package com.moonspoon.moonspoon.controller;

import com.moonspoon.moonspoon.dto.request.UserSignupRequest;
import com.moonspoon.moonspoon.dto.response.UserResponse;
import com.moonspoon.moonspoon.service.UserService;
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

    @PostMapping("/singup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserSignupRequest dto){
        UserResponse response = userService.signup(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
