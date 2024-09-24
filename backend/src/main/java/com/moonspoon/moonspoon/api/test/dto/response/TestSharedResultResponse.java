package com.moonspoon.moonspoon.api.test.dto.response;

import com.moonspoon.moonspoon.core.problem.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestSharedResultResponse {
    private Long id;
    private String question;
    private String solution;
    private String input;
    private String result;

    @Builder
    public TestSharedResultResponse(Long id, String question, String solution, String input, String result) {
        this.id = id;
        this.question = question;
        this.solution = solution;
        this.input = input;
        this.result = result;
    }

    public static TestSharedResultResponse fromEntity(Problem problem){
        return TestSharedResultResponse.builder()
                .id(problem.getId())
                .question(problem.getQuestion())
                .solution(problem.getSolution())
                .build();
    }
}
