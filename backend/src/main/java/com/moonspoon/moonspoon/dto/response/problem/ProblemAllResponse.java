package com.moonspoon.moonspoon.dto.response.problem;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class ProblemAllResponse {
    String workbookTitle;
    Page<ProblemResponse> problems;
}
