package com.moonspoon.moonspoon.dto.request.workbook;

import com.moonspoon.moonspoon.domain.Workbook;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkbookCreateRequest {

    @NotBlank(message = "문제집 이름은 필수 입력입니다.")
    private String title;
    private String content;
    private LocalDateTime createDate;

    public static Workbook toEntity(WorkbookCreateRequest dto){
        return Workbook.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .createDate(dto.getCreateDate())
                .build();
    }


}
