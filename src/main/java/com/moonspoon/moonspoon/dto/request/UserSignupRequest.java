package com.moonspoon.moonspoon.dto.request;

import com.moonspoon.moonspoon.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {
    private String username;
    private String name;
    private String password;

    public static User toEntity(UserSignupRequest dto){
        return User.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .password(dto.getPassword())
                .build();
    }
}
