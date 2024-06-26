package com.moonspoon.moonspoon.dto.response;

import com.moonspoon.moonspoon.domain.Workbook;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class WorkbookResponse {
    private String title;
    private String content;
    private LocalDateTime createDate;

    @Builder
    public WorkbookResponse(String title, String content, LocalDateTime createDate){
        this.title = title;
        this.content = content;
        this.createDate = createDate;
    }

    public static WorkbookResponse fromEntity(Workbook workbook){
        return WorkbookResponse.builder()
                .title(workbook.getTitle())
                .content(workbook.getContent())
                .createDate(workbook.getCreateDate())
                .build();
    }


}
