package com.moonspoon.moonspoon.dto.response.notice;

import com.moonspoon.moonspoon.notice.Notice;
import com.moonspoon.moonspoon.user.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeResponse {
    private String title;
    private String content;
    private String author;
    private String role;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Builder
    public NoticeResponse(String title, String content, String author,
                          String role,
                          LocalDateTime createDate, LocalDateTime updateDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.role = role;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static NoticeResponse fromEntity(Notice notice){
        return NoticeResponse.builder()
                .author(notice.getAuthor())
                .title(notice.getTitle())
                .content(notice.getContent())
                .role(notice.getUser().getRole().getValue())
                .createDate(notice.getCreateDate())
                .updateDate(notice.getUpdateDate())
                .build();
    }

}
