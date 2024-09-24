package com.moonspoon.moonspoon.api.sharedWorkbook.dto.response;

import com.moonspoon.moonspoon.api.comment.dto.response.CommentResponse;
import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbook;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SharedWorkbookResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private int problemCount;
    private boolean isRandom;

    private boolean isUser;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private List<CommentResponse> comments;
    @Builder
    public SharedWorkbookResponse(Long id, String title, String content, String author,
                                  int problemCount,
                                  boolean isRandom,
                                  LocalDateTime createDate, LocalDateTime updateDate, boolean isUser) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.problemCount = problemCount;
        this.isRandom = isRandom;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.isUser = isUser;
    }

    public static SharedWorkbookResponse fromEntity(SharedWorkbook sharedWorkbook){
        return SharedWorkbookResponse.builder()
                .id(sharedWorkbook.getId())
                .title(sharedWorkbook.getTitle())
                .content(sharedWorkbook.getContent())
                .author(sharedWorkbook.getAuthor())
                .isRandom(sharedWorkbook.isRandom())
                .createDate(sharedWorkbook.getCreateDate())
                .updateDate(sharedWorkbook.getUpdateDate())
                .build();

    }

}
