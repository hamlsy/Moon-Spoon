package com.moonspoon.moonspoon.api.comment.dto.response;

import com.moonspoon.moonspoon.core.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private Long id;
    private Long sharedWorkbookId;
    private String content;
    private String author;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public CommentResponse(Long id, Long sharedWorkbookId, String content, String author,
                           LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.sharedWorkbookId = sharedWorkbookId;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static CommentResponse fromEntity(Comment comment){
        return CommentResponse.builder()
                .content(comment.getContent())
                .author(comment.getAuthor())
                .id(comment.getId())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}
