package com.moonspoon.moonspoon.dto.request;

import com.moonspoon.moonspoon.domain.Workbook;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkbookUpdateRequest {

    private String title;
    private String content;
    private LocalDateTime updateDate;

    public static Workbook toEntity(WorkbookUpdateRequest dto){
        return Workbook.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .updateDate(dto.getUpdateDate())
                .build();
    }
}
