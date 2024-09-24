package com.moonspoon.moonspoon.api.sharedWorkbook.dto.request;

import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbook;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedWorkbookRequest {
    private Long workbookId;

    @NotBlank(message = "제목은 필수 입력사항입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력사항입니다.")
    private String content;

    private boolean random;
    public static SharedWorkbook toEntity(SharedWorkbookRequest dto){
        return SharedWorkbook.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .random(dto.isRandom())
                .build();
    }

}
