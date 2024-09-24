package com.moonspoon.moonspoon.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DuplicateErrorResponse {
    private String message;
    private boolean validate;

}
