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
    private boolean isRandom;
    private boolean hasSolution;

    private LocalDateTime sharedDate;
    private LocalDateTime updateDate;

    @Builder
    public SharedWorkbookResponse(Long id, String title, String content, String author,
                                  boolean isRandom, boolean hasSolution,
                                  LocalDateTime sharedDate, LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.isRandom = isRandom;
        this.hasSolution = hasSolution;

        this.sharedDate = sharedDate;
        this.updateDate = updateDate;
    }

    public static SharedWorkbookResponse fromEntity(SharedWorkbook sharedWorkbook){
        return SharedWorkbookResponse.builder()
                .id(sharedWorkbook.getId())
                .title(sharedWorkbook.getTitle())
                .content(sharedWorkbook.getContent())
                .author(sharedWorkbook.getAuthor())
                .isRandom(sharedWorkbook.isRandom())
                .hasSolution(sharedWorkbook.isHasSolution())
                .sharedDate(sharedWorkbook.getSharedDate())
                .updateDate(sharedWorkbook.getUpdateDate())
                .build();

    }

}
