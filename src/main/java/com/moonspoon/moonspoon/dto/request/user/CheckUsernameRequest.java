package com.moonspoon.moonspoon.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckUsernameRequest {
    @NotBlank(message = "아이디를 입력하세요.")
    private String username;
}
