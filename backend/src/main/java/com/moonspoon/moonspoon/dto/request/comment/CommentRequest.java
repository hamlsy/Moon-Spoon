package com.moonspoon.moonspoon.dto.request.comment;

import com.moonspoon.moonspoon.comment.Comment;
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
