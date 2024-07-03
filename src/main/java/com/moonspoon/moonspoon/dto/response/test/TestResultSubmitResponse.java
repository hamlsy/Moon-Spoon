package com.moonspoon.moonspoon.dto.response.test;

import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestResultSubmitResponse {
    private int correctCount;
    private int incorrectCount;
    private double score;
    private List<TestResultSubmitRequest> problems;

    @Builder
    public TestResultSubmitResponse(int correctCount, int incorrectCount , List<TestResultSubmitRequest> problems) {
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
        this.score = score;
        this.problems = problems;
    }

}
