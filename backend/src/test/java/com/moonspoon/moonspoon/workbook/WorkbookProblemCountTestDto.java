package com.moonspoon.moonspoon.workbook;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class WorkbookProblemCountTestDto {
    private Long workbookId;
    private Long problemCount;

    public WorkbookProblemCountTestDto(Long workbookId, Long problemCount) {
        this.workbookId = workbookId;
        this.problemCount = problemCount;
    }
}
