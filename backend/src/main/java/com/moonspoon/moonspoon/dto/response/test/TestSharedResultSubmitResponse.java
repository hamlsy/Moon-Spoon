package com.moonspoon.moonspoon.dto.response.test;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestSharedResultSubmitResponse {
    private Long id;
    private int correctCount;
    private int incorrectCount;
    private double score;

    @Builder
    public TestSharedResultSubmitResponse(Long id, int correctCount, int incorrectCount, double score) {
        this.id = id;
        this.correctCount = correctCount;
        this.incorrectCount = incorrectCount;
        this.score = score;
    }
}
