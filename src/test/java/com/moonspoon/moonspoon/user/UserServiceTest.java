package com.moonspoon.moonspoon.user;

import com.moonspoon.moonspoon.TestUserRepository;
import com.moonspoon.moonspoon.TestUserService;
import com.moonspoon.moonspoon.repository.UserRepository;
import com.moonspoon.moonspoon.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TestUserRepository testUserRepository;

    @Test
    void concurrentSignupTest(){


    }

}
