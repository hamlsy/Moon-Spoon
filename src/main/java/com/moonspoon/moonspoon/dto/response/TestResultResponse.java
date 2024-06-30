package com.moonspoon.moonspoon.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResultResponse {
    private Long id;
    private String question;
    private String solution;
    private String correctRate;

    private String input;
    private String result;

    @Builder
    public TestResultResponse(Long id, String question, String solution, String correctRate, String input, String result) {
        this.id = id;
        this.question = question;
        this.solution = solution;
        this.correctRate = correctRate;
        this.input = input;
        this.result = result;
    }
}
