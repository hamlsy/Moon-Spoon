package com.moonspoon.moonspoon.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;

}
