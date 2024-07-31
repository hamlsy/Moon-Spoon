package com.moonspoon.moonspoon.dto.response.notice;

import com.moonspoon.moonspoon.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeResponse {
    String title;
    String content;
    String author;
    LocalDateTime createDate;
    LocalDateTime updateDate;

    @Builder
    public NoticeResponse(String title, String content, String author, LocalDateTime createDate, LocalDateTime updateDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static NoticeResponse fromEntity(Notice notice){
        return NoticeResponse.builder()
                .author(notice.getAuthor())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createDate(notice.getCreateDate())
                .updateDate(notice.getUpdateDate())
                .build();
    }

}
