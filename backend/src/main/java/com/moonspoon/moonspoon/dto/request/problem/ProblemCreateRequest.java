package com.moonspoon.moonspoon.dto.request.problem;

import com.moonspoon.moonspoon.problem.Problem;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProblemCreateRequest {

    @NotBlank(message = "문제 이름은 필수 입력 사항입니다.")
    private String question;

    @NotBlank(message = "정답은 필수 입력 사항입니다.")
    private String solution;

    public static Problem toEntity(ProblemCreateRequest dto){
        return Problem.builder()
                .question(dto.getQuestion())
                .solution(dto.getSolution())
                .createDate(LocalDateTime.now())
                .build();
    }
}
