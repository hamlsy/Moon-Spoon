package com.moonspoon.moonspoon.common.validation;

import com.moonspoon.moonspoon.common.exception.custom.NotUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.moonspoon.moonspoon.common.utils.Utils.getCurrentUsername;

@Component
public class UserValidator {

    private static final String ANONYMOUS_USER = "anonymousUser";
    private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다.";


    public void validateCurrentUser(){
        String username = getCurrentUsername();
        if(username == null || username.equals(ANONYMOUS_USER)){
            throw new NotUserException(UNAUTHORIZED_MESSAGE);
        }
    }

}
