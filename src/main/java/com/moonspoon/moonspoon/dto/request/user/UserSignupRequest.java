package com.moonspoon.moonspoon.dto.request.user;

import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {

    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 2, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자로 이루어져야 합니다.")
    private String username;

    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 4, max = 16)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s_-]+$", message = "특수문자는 허용되지 않습니다.")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 6, max = 24)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "비밀번호는 영문과 숫자로 이루어져야 합니다.")
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
