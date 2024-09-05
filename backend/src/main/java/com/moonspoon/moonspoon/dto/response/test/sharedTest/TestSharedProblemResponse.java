package com.moonspoon.moonspoon.dto.response.test.sharedTest;

import com.moonspoon.moonspoon.problem.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestSharedProblemResponse {
    private Long id;
    private String question;

    @Builder
    public TestSharedProblemResponse(Long id, String question) {
        this.id = id;
        this.question = question;
    }

    public static TestSharedProblemResponse fromEntity(Problem problem){
        return TestSharedProblemResponse.builder()
                .id(problem.getId())
                .question(problem.getQuestion())
                .build();
    }
}
