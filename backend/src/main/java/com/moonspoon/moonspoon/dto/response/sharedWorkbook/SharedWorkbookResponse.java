package com.moonspoon.moonspoon.dto.response.sharedWorkbook;

import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SharedWorkbookResponse {
    private String title;
    private String content;
    private String author;
    private LocalDateTime sharedDate;
    private LocalDateTime updateDate;

    @Builder
    public SharedWorkbookResponse(String title, String content, String author, LocalDateTime sharedDate, LocalDateTime updateDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.sharedDate = sharedDate;
        this.updateDate = updateDate;
    }

    public static SharedWorkbookResponse fromEntity(SharedWorkbook sharedWorkbook){
        return SharedWorkbookResponse.builder()
                .title(sharedWorkbook.getTitle())
                .content(sharedWorkbook.getContent())
                .author(sharedWorkbook.getAuthor())
                .sharedDate(sharedWorkbook.getSharedDate())
                .updateDate(sharedWorkbook.getUpdateDate())
                .build();

    }

}
