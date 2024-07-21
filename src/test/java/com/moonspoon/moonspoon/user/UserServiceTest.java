package com.moonspoon.moonspoon.user;

import com.moonspoon.moonspoon.TestUser;
import com.moonspoon.moonspoon.TestUserRepository;
import com.moonspoon.moonspoon.TestUserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserServiceTest {
    @Autowired
    private TestUserService service;

    @Autowired
    private TestUserRepository repository;

    @Test
    @DisplayName("유니크 제약 테스트")
    void concurrentSignupTest() throws InterruptedException{
        int threadCount = 200;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    TestUser user = new TestUser();
                    user.setName("userA");
                    service.signupUnique(user);
                } catch (Exception e) {
                    System.out.println("Exception occurred: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Unique Key Time: " + (endTime - startTime) + "ms");
        latch.await();
        assertEquals(1, repository.count());

    }
    @Test
    @DisplayName("Synchronized 테스트")
    void concurrentSignupSynTest() throws InterruptedException{
        int threadCount = 200;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    TestUser user = new TestUser();
                    user.setSynName("userA");
                    service.signupSynchronized(user);
                } catch (Exception e) {
                    System.out.println("Exception occurred: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Synchronized Time: " + (endTime - startTime) + "ms");
        latch.await();
        assertEquals(1, repository.count());

    }
    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void contextLoads() {
    }
}
