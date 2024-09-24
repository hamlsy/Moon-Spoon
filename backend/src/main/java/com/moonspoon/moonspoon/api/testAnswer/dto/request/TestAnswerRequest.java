package com.moonspoon.moonspoon.api.testAnswer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestAnswerRequest {
    private Long problemId;
    private String answer;
}
