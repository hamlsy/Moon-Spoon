package com.moonspoon.moonspoon.dto.response.test;

import com.moonspoon.moonspoon.domain.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResultSubmitResponse {
    private Long id;
    private String result;
    private int correctCount;
    private int incorrectCount;
    private double correctRate;

    @Builder
    public TestResultSubmitResponse(Long id, String result, int correctCount, int incorrectCount, double correctRate) {
        this.id = id;
        this.result = result;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
        this.correctRate = correctRate;
    }

    public static TestResultSubmitResponse fromEntity(Problem problem){
        return TestResultSubmitResponse.builder()
                .correctCount(problem.getCorrectCount())
                .incorrectCount(problem.getIncorrectCount())
                .id(problem.getId())
                .correctRate(problem.getCorrectRate())
                .build();
    }
}
