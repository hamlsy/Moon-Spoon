package com.moonspoon.moonspoon.dto.request.notice;

import com.moonspoon.moonspoon.notice.Notice;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeCreateRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    @NotBlank(message = "내용을 입력해주세요.")
    String content;

    public static Notice toEntity(NoticeCreateRequest request){
        return Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
