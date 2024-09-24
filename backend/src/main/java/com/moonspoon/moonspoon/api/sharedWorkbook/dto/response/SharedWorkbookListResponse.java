package com.moonspoon.moonspoon.api.sharedWorkbook.dto.response;

import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbook;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SharedWorkbookListResponse {
    private Long id;
    private String title;
    private String content;
    private String author;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public SharedWorkbookListResponse(Long id, String title, String content, String author,
                                  LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static SharedWorkbookListResponse fromEntity(SharedWorkbook sharedWorkbook){
        return SharedWorkbookListResponse.builder()
                .id(sharedWorkbook.getId())
                .title(sharedWorkbook.getTitle())
                .content(sharedWorkbook.getContent())
                .author(sharedWorkbook.getAuthor())
                .createDate(sharedWorkbook.getCreateDate())
                .updateDate(sharedWorkbook.getUpdateDate())
                .build();

    }

}
