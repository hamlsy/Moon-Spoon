package com.moonspoon.moonspoon.dto.request.problem;

import com.moonspoon.moonspoon.domain.Problem;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemUpdateRequest {
    @NotBlank(message = "문제 이름은 필수 입력 사항입니다.")
    private String question;

    @NotBlank(message = "정답은 필수 입력 사항입니다.")
    private String solution;

}
