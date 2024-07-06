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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TestUserRepository testUserRepository;

    @Test
    void concurrentSignupTest() throws InterruptedException{
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    TestUser user = new TestUser();
                    user.setName("name" + index);
                    // 회원가입 시도
                    testUserService.signup(user);
                } catch (Exception e) {
                    // 예외 처리
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 모든 쓰레드가 실행을 완료한 후, 회원 수 검증
        assertEquals(threadCount, testUserRepository.count());
    }
}
