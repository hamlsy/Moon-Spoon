package com.moonspoon.moonspoon.api.sharedWorkbook.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedWorkbookUpdateRequest {
    private String title;
    private String content;
    private boolean random;
}
