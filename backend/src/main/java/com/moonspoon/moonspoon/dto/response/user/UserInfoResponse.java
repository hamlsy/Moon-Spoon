package com.moonspoon.moonspoon.dto.response.user;

import com.moonspoon.moonspoon.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponse {
    String username;
    String name;
    String role;
    int myWorkbookCount;
    int sharedWorkbookCount;

    @Builder
    public UserInfoResponse(String username, String name, String role,
                            int myWorkbookCount, int sharedWorkbookCount) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.myWorkbookCount = myWorkbookCount;
        this.sharedWorkbookCount = sharedWorkbookCount;
    }

    public static UserInfoResponse fromEntity(User user){
        return UserInfoResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole().getValue())
                .myWorkbookCount(user.getWorkbooks().size())
                .build();
    }
}
