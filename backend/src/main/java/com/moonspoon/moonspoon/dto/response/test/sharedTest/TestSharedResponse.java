package com.moonspoon.moonspoon.dto.response.test.sharedTest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestSharedResponse {
    private Long testId;
    private List<TestSharedProblemResponse> testSharedProblems;

    @Builder
    public TestSharedResponse(Long testId, List<TestSharedProblemResponse> testSharedProblems) {
        this.testId = testId;
        this.testSharedProblems = testSharedProblems;
    }
}
