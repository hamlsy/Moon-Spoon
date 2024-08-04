package com.moonspoon.moonspoon.dto.request.sharedWorkbook;

import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
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

    private boolean isRandom;
    private boolean hasRandom;
    public static SharedWorkbook toEntity(SharedWorkbookRequest dto){
        return SharedWorkbook.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .isRandom(dto.isRandom())
                .hasRandom(dto.hasRandom)
                .build();
    }

}
