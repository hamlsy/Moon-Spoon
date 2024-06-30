package com.moonspoon.moonspoon.dto.response;

import com.moonspoon.moonspoon.domain.Workbook;
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

    @Builder
    public WorkbookResponse(Long id, String title, int problemCount, String content, LocalDateTime createDate){
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.problemCount = problemCount;
        this.id = id;
    }

    public static WorkbookResponse fromEntity(Workbook workbook){
        return WorkbookResponse.builder()
                .id(workbook.getId())
                .title(workbook.getTitle())
                .content(workbook.getContent())
                .problemCount(workbook.getProblems().size())
                .createDate(workbook.getCreateDate())
                .build();
    }


}
