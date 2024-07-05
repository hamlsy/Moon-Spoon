package com.moonspoon.moonspoon.dto.response.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCheckResponse {
    private String message;
    private boolean validate;
}
