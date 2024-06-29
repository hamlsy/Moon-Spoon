package com.moonspoon.moonspoon.dto.response;

import com.moonspoon.moonspoon.domain.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProblemResponse {
    private String question;
    private String solution;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public ProblemResponse(String question, String solution, LocalDateTime createDate, LocalDateTime updateDate) {
        this.question = question;
        this.solution = solution;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static ProblemResponse fromEntity(Problem problem){
        return ProblemResponse.builder()
                .question(problem.getQuestion())
                .solution(problem.getSolution())
                .createDate(problem.getCreateDate())
                .updateDate(problem.getUpdateDate())
                .build();
    }
}
