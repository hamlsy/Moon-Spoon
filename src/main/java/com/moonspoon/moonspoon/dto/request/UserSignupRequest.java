package com.moonspoon.moonspoon.dto.request;

import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {

    @NotBlank(message = "아이디를 입력하세요.")
    private String username;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    public static User toEntity(UserSignupRequest dto){
        return User.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .role(UserRole.USER)
                .password(dto.getPassword())
                .build();
    }
}
