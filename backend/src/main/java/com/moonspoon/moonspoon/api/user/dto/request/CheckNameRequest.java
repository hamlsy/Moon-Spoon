package com.moonspoon.moonspoon.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckNameRequest {
    @NotBlank(message = "이름(닉네임)을 입력하세요.")
    @Size(min = 2, max = 16)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s_-]+$", message = "특수문자는 허용되지 않습니다.")
    private String name;
}
