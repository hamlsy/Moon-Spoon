package com.moonspoon.moonspoon.dto.response.user;

import com.moonspoon.moonspoon.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

    @Builder
    public UserProfileResponse(String username, String name, LocalDateTime signupDate,
                               String role, int visitCount, int workbookCount,
                               int sharedWorkbookCount, int workbookTestCount,
                               int sharedWorkbookTestCount) {
        this.username = username;
        this.name = name;
        this.signupDate = signupDate;
        this.role = role;
        this.visitCount = visitCount;
        this.workbookCount = workbookCount;
        this.sharedWorkbookCount = sharedWorkbookCount;
        this.workbookTestCount = workbookTestCount;
        this.sharedWorkbookTestCount = sharedWorkbookTestCount;

    }

    public static UserProfileResponse fromEntity(User user){
        return UserProfileResponse.builder()
                .username(user.getUsername())
                .signupDate(user.getSignupDate())
                .workbookTestCount(user.getWorkbookTestCount())
                .name(user.getName())
                .build();
    }
}
