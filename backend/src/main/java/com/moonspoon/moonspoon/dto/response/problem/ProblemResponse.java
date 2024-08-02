package com.moonspoon.moonspoon.dto.response.problem;

import com.moonspoon.moonspoon.problem.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProblemResponse {
    private Long id;
    private String question;
    private String solution;
    private double correctRate;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public ProblemResponse(String question, String solution, LocalDateTime createDate, LocalDateTime updateDate, double correctRate, Long id) {
        this.id = id;
        this.question = question;
        this.solution = solution;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.correctRate = correctRate;
    }

    public static ProblemResponse fromEntity(Problem problem){
        return ProblemResponse.builder()
                .id(problem.getId())
                .question(problem.getQuestion())
                .solution(problem.getSolution())
                .correctRate(problem.getCorrectRate())
                .createDate(problem.getCreateDate())
                .updateDate(problem.getUpdateDate())
                .build();
    }
}
