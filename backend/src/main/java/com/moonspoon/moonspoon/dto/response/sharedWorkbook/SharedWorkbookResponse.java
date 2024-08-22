package com.moonspoon.moonspoon.dto.response.sharedWorkbook;

import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SharedWorkbookResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private int problemCount;
    private boolean isRandom;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public SharedWorkbookResponse(Long id, String title, String content, String author,
                                  int problemCount,
                                  boolean isRandom,
                                  LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.problemCount = problemCount;
        this.isRandom = isRandom;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static SharedWorkbookResponse fromEntity(SharedWorkbook sharedWorkbook){
        return SharedWorkbookResponse.builder()
                .id(sharedWorkbook.getId())
                .title(sharedWorkbook.getTitle())
                .content(sharedWorkbook.getContent())
                .author(sharedWorkbook.getAuthor())
                .isRandom(sharedWorkbook.isRandom())
                .problemCount(sharedWorkbook.getWorkbook().getProblems().size())
                .createDate(sharedWorkbook.getCreateDate())
                .updateDate(sharedWorkbook.getUpdateDate())
                .build();

    }

}
