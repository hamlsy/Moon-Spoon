package com.moonspoon.moonspoon.dto.response.test.sharedTest;

import com.moonspoon.moonspoon.problem.Problem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TestSharedPracticeResponse {
    private Long id;
    private String question;
    private String solution;

    @Builder
    public TestSharedPracticeResponse(Long id, String question, String solution) {
        this.id = id;
        this.question = question;
        this.solution = solution;
    }

    public static TestSharedPracticeResponse fromEntity(Problem problem){
        return TestSharedPracticeResponse.builder()
                .id(problem.getId())
                .question(problem.getQuestion())
                .solution(problem.getSolution())
                .build();
    }
}
