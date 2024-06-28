package com.moonspoon.moonspoon.dto.request;

import com.moonspoon.moonspoon.domain.Problem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemUpdateRequest {
    private String question;
    private String solution;

}
