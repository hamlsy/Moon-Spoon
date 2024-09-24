package com.moonspoon.moonspoon.api.test.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestRequest {
    private Long id;
    private boolean random;
    private int problemCount;
    private String sortOrder;

}
