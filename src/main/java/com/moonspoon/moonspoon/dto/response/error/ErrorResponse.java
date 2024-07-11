package com.moonspoon.moonspoon.dto.response.error;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;

}
