package com.moonspoon.moonspoon.dto.response.problem;

import com.moonspoon.moonspoon.problem.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProblemCreateResponse {
    private String question;
    private String solution;
    private LocalDateTime createDate;

    @Builder
    public ProblemCreateResponse(String question, String solution, LocalDateTime createDate) {
        this.question = question;
        this.solution = solution;
        this.createDate = createDate;
    }

    public static ProblemCreateResponse fromEntity(Problem problem){
        return ProblemCreateResponse.builder()
                .question(problem.getQuestion())
                .solution(problem.getSolution())
                .createDate(problem.getCreateDate())
                .build();
    }
}
