package com.moonspoon.moonspoon.user;

import com.moonspoon.moonspoon.TestUser;
import com.moonspoon.moonspoon.TestUserRepository;
import com.moonspoon.moonspoon.TestUserService;
import com.moonspoon.moonspoon.repository.UserRepository;
import com.moonspoon.moonspoon.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private TestUserService service;

    @Autowired
    private TestUserRepository repository;

    @Test
    @Transactional
    void concurrentSignupTest() throws InterruptedException{
        //given
        TestUser user = new TestUser();
        user.setName("userA");
        int threadCount = 20;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    service.signup(user);
                } catch (Exception e) {
                    // Expected exception for duplicate names
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        assertEquals(1, repository.count());

    }
}
