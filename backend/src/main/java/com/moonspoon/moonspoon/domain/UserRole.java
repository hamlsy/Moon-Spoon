package com.moonspoon.moonspoon.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMIN", "admin"),
    USER("ROLE_USER", "user");
    private final String key;
    private final String value;
}
