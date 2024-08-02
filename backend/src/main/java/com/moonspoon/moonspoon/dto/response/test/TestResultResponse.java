package com.moonspoon.moonspoon.dto.response.test;

import com.moonspoon.moonspoon.problem.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResultResponse {
    private Long id;
    private String question;
    private String solution;
    private double correctRate;

    private String input;
    private String result;

    @Builder
    public TestResultResponse(Long id, String question, String solution, double correctRate, String input, String result) {
        this.id = id;
        this.question = question;
        this.solution = solution;
        this.correctRate = correctRate;
        this.input = input;
        this.result = result;
    }

    public static TestResultResponse fromEntity(Problem problem){
        return TestResultResponse.builder()
                .id(problem.getId())
                .question(problem.getQuestion())
                .solution(problem.getSolution())
                .correctRate(problem.getCorrectRate())
                .build();
    }
}
