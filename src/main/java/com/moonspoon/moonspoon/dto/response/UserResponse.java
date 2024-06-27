package com.moonspoon.moonspoon.dto.response;

import com.moonspoon.moonspoon.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String username;
    private String name;
    private String password;

    @Builder
    public UserResponse(String username, String name, String password){
        this.username = username;
        this.name = name;
        this.password = password;
    }


    public static UserResponse fromEntity(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }
}
