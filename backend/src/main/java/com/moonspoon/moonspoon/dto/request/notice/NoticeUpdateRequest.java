package com.moonspoon.moonspoon.dto.request.notice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeUpdateRequest {
    String title;
    String content;
}
