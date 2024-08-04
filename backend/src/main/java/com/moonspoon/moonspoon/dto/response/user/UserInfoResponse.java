package com.moonspoon.moonspoon.dto.response.user;

import com.moonspoon.moonspoon.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponse {
    private String username;
    private String name;
    private String role;
    private LocalDateTime signupDate;
    private int myWorkbookCount;
    private int sharedWorkbookCount;


    @Builder
    public UserInfoResponse(String username, String name, String role,
                            int myWorkbookCount, int sharedWorkbookCount,
                            LocalDateTime signupDate) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.myWorkbookCount = myWorkbookCount;
        this.sharedWorkbookCount = sharedWorkbookCount;
        this.signupDate = signupDate;
    }

    public static UserInfoResponse fromEntity(User user){
        return UserInfoResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .signupDate(user.getSignupDate())
                .role(user.getRole().getValue())
                .myWorkbookCount(user.getWorkbooks().size())
                .build();
    }
}
