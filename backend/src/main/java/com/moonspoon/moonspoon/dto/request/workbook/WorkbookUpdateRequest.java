package com.moonspoon.moonspoon.dto.request.workbook;

import com.moonspoon.moonspoon.workbook.Workbook;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkbookUpdateRequest {

    @NotBlank(message = "문제집 이름은 필수 입력입니다.")
    private String title;
    private String content;
    private LocalDateTime updateDate;

    public static Workbook toEntity(WorkbookUpdateRequest dto){
        return Workbook.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .updateDate(dto.getUpdateDate())
                .build();
    }
}
