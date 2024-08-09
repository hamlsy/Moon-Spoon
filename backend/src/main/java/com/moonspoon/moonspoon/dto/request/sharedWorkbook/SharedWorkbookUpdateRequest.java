package com.moonspoon.moonspoon.dto.request.sharedWorkbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedWorkbookUpdateRequest {
    private String title;
    private String content;
    private boolean isRandom;
    private boolean hasSolution;
}
