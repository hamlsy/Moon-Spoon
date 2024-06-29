package com.moonspoon.moonspoon.dto.response;

import com.moonspoon.moonspoon.domain.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestProblemResponse {
    private Long id;
    private String question;
    private double correctRate;

    @Builder
    public TestProblemResponse(String question, double correctRate, Long id) {
        this.id = id;
        this.question = question;
        this.correctRate = correctRate;
    }

    public static TestProblemResponse fromEntity(Problem problem){
        return TestProblemResponse.builder()
                .question(problem.getQuestion())
                .id(problem.getId())
                .correctRate(problem.getCorrectRate())
                .build();
    }
}
