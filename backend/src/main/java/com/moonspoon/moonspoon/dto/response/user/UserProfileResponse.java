package com.moonspoon.moonspoon.dto.response.user;

import com.moonspoon.moonspoon.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserProfileResponse {
    private String username;
    private String name;
    private LocalDateTime signupDate;
    private String role;

    private int visitCount;

    private int workbookCount;
    private int sharedWorkbookCount;
    private int workbookTestCount;
    private int sharedWorkbookTestCount;

    public UserProfileResponse(Long workbookCount,
                               Long sharedWorkbookCount,
                               Long sharedWorkbookTestCount) {
        this.workbookCount = longToInt(workbookCount);
        this.sharedWorkbookCount = longToInt(sharedWorkbookCount);
        this.sharedWorkbookTestCount = longToInt(sharedWorkbookTestCount);

    }

    private int longToInt(Long num){
        return (num != null) ? num.intValue() : 0;
    }


}
