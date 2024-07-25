package com.moonspoon.moonspoon.dto.response.test;

import com.moonspoon.moonspoon.domain.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TestProblemResponse {
    private Long id;
    private String question;
    private double correctRate;
    private LocalDateTime createDate;
    @Builder
    public TestProblemResponse(String question, double correctRate, Long id, LocalDateTime createDate) {
        this.id = id;
        this.question = question;
        this.correctRate = correctRate;
        this.createDate = createDate;
    }

    public static TestProblemResponse fromEntity(Problem problem){
        return TestProblemResponse.builder()
                .question(problem.getQuestion())
                .id(problem.getId())
                .createDate(problem.getCreateDate())
                .correctRate(problem.getCorrectRate())
                .build();
    }
}
