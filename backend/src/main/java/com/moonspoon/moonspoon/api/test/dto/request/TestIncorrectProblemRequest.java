package com.moonspoon.moonspoon.api.test.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class TestIncorrectProblemRequest {
    private List<Long> incorrectProblemIds;
}
