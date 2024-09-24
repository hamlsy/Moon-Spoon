package com.moonspoon.moonspoon.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckUsernameRequest {
    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자로 이루어져야 합니다.")
    private String username;
}
