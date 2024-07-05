package com.moonspoon.moonspoon.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckNameRequest {
    @NotBlank(message = "이름(닉네임)을 입력하세요.")
    private String name;
}
