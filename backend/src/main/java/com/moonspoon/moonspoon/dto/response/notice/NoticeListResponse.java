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
public class NoticeListResponse {
    private String title;
    private String author;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public NoticeListResponse(String title, String author, LocalDateTime createDate, LocalDateTime updateDate) {
        this.title = title;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static NoticeListResponse fromEntity(Notice notice){
        return NoticeListResponse.builder()
                .author(notice.getAuthor())
                .title(notice.getTitle())
                .createDate(notice.getCreateDate())
                .updateDate(notice.getUpdateDate())
                .build();
    }
}
