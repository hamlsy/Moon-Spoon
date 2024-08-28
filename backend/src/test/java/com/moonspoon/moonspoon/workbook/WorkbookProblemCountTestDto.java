package com.moonspoon.moonspoon.workbook;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class WorkbookProblemCountTestDto {
    private Long workbookId;
    private Long problemCount;

    public Long getWorkbookId() {
        return workbookId;
    }

    public Long getProblemCount() {
        return problemCount;
    }

    public WorkbookProblemCountTestDto(Long workbookId, Long problemCount) {
        this.workbookId = workbookId;
        this.problemCount = problemCount;
    }
}
