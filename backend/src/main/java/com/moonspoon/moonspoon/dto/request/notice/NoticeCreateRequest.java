package com.moonspoon.moonspoon.dto.request.notice;

import com.moonspoon.moonspoon.notice.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeCreateRequest {
    String title;
    String content;

    public static Notice toEntity(NoticeCreateRequest request){
        return Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
