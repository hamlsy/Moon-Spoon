package com.moonspoon.moonspoon.api.sharedWorkbook.dto.response;

import com.moonspoon.moonspoon.core.problem.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedWorkbookTestResponse {
    private Long id;
    private String question;

    @Builder
    public SharedWorkbookTestResponse(Long id, String question) {
        this.id = id;
        this.question = question;
    }

    public static SharedWorkbookTestResponse fromEntity(Problem problem){
        return SharedWorkbookTestResponse.builder()
                .id(problem.getId())
                .question(problem.getQuestion())
                .build();
    }
}
