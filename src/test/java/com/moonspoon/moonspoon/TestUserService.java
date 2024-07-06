package com.moonspoon.moonspoon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestUserService {
    @Autowired
    private TestUserRepository testUserRepository;

    @Transactional
    public TestUser signup(TestUser testUser){
        return testUserRepository.save(testUser);
    }


}
