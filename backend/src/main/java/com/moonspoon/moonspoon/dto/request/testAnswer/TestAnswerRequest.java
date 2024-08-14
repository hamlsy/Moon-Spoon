package com.moonspoon.moonspoon.dto.request.testAnswer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestAnswerRequest {
    private Long problemId;
    private String answer;
}
