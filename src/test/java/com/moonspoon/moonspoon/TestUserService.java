package com.moonspoon.moonspoon;

import com.moonspoon.moonspoon.exception.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestUserService {
    @Autowired
    private TestUserRepository repository;

    @Transactional
    public void signup(TestUser testUser){
        if(repository.existsByName(testUser.getName())){
            throw new DuplicateUserException("존재하는 이름입니다.");
        }
        repository.save(testUser);
    }


}
