package com.moonspoon.moonspoon.api.workbook.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class WorkbookProblemCountDto {
    private Long workbookId;
    private Long problemCount;
}
