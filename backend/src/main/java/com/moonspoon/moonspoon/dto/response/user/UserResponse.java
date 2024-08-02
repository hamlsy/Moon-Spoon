package com.moonspoon.moonspoon.dto.response.user;

import com.moonspoon.moonspoon.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String username;
    private String name;
    private String password;
    private String role;

    @Builder
    public UserResponse(String username, String name, String password, String role){
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }


    public static UserResponse fromEntity(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole().getValue())
                .password(user.getPassword())
                .build();
    }
}
