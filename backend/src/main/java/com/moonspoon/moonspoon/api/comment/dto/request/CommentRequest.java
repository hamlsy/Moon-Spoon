package com.moonspoon.moonspoon.api.comment.dto.request;

import com.moonspoon.moonspoon.core.comment.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private Long sharedWorkbookId;
    private String content;

    public static Comment toEntity(CommentRequest dto){
        return Comment.builder()
                .content(dto.content)
                .build();
    }

}
