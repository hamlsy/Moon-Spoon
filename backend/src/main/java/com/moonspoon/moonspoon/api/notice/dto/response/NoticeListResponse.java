package com.moonspoon.moonspoon.api.notice.dto.response;

import com.moonspoon.moonspoon.core.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeListResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public NoticeListResponse(Long id, String title, String author, LocalDateTime createDate,
                              LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static NoticeListResponse fromEntity(Notice notice){
        return NoticeListResponse.builder()
                .id(notice.getId())
                .author(notice.getAuthor())
                .title(notice.getTitle())
                .createDate(notice.getCreateDate())
                .updateDate(notice.getUpdateDate())
                .build();
    }
}
