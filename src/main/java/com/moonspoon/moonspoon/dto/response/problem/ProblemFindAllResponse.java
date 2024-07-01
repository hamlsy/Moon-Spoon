package com.moonspoon.moonspoon.dto.response.problem;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProblemFindAllResponse {
    private String workbookTitle;
    private List<ProblemResponse> problems;

}
