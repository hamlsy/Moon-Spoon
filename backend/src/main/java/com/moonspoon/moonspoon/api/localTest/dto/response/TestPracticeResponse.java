package com.moonspoon.moonspoon.api.localTest.dto.response;

import com.moonspoon.moonspoon.core.problem.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestPracticeResponse {
    private Long id;
    private String question;
    private String solution;
    private double correctRate;

    @Builder
    public TestPracticeResponse(Long id, String question, String solution, double correctRate) {
        this.id = id;
        this.question = question;
        this.solution = solution;
        this.correctRate = correctRate;
    }

    public static TestPracticeResponse fromEntity(Problem problem){
        return TestPracticeResponse.builder()
                .id(problem.getId())
                .correctRate(problem.getCorrectRate())
                .question(problem.getQuestion())
                .solution(problem.getSolution())
                .build();
    }
}
