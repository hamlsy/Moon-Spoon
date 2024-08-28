package com.moonspoon.moonspoon.dto.response;

import com.moonspoon.moonspoon.workbook.Workbook;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class WorkbookResponse {
    private Long id;
    private String title;
    private String content;
    private int problemCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public WorkbookResponse(Long id, String title, int problemCount, String content, LocalDateTime createDate, LocalDateTime updateDate){
        this.id = id;
        this.title = title;
        this.content = content;
        this.problemCount = problemCount;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static WorkbookResponse fromEntity(Workbook workbook){
        return WorkbookResponse.builder()
                .id(workbook.getId())
                .title(workbook.getTitle())
                .content(workbook.getContent())
                .updateDate(workbook.getUpdateDate())
                .createDate(workbook.getCreateDate())
                .build();
    }


}
