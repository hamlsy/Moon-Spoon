package com.moonspoon.moonspoon.api.localTest.dto.response;

import com.moonspoon.moonspoon.api.test.dto.request.TestResultSubmitRequest;
import lombok.Builder;
import lombok.Getter;
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
    public TestResultSubmitResponse(int correctCount, int incorrectCount , List<TestResultSubmitRequest> problems, double score) {
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
        this.score = score;
        this.problems = problems;
    }

}
