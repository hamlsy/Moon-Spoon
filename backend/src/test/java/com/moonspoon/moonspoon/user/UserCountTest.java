package com.moonspoon.moonspoon.user;

import com.moonspoon.moonspoon.workbook.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
public class UserCountTest {
    @Autowired
    private UserRepositoryTest repository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        this.user = userRepository.findByUsername("dddd");
    }

    @Test
    @DisplayName("개별 카운트")
    void countEach(){
        //given
        Long id = this.user.getId();

        long startTime = System.currentTimeMillis();

        //when
        // 조회
        Long testCount = repository.testCountByUserId(id);
        Long sharedWorkbookCount = repository.sharedWorkbookCountByUserId(id);
        Long workbookCount = repository.workbookCountByUserId(id);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("조회시간 " + (endTime-startTime) + "ms");
        System.out.println("테스트 카운트 " + testCount);
        System.out.println("공유 문제집 카운트 " + sharedWorkbookCount);
        System.out.println("문제집 카운트 " + workbookCount);
    }


    @Test
    @DisplayName("한번에 카운트")
    void countOnce(){
        //given
        Long id = this.user.getId();

        long startTime = System.currentTimeMillis();

        //when
        // 조회
        UserInfoDtoTest dto = repository.userInfoCountByUserId(id);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("조회시간 " + (endTime-startTime) + "ms");
        System.out.println("테스트 카운트 " + dto.getTestCount());
        System.out.println("공유 문제집 카운트 " + dto.getSharedWorkbookCount());
        System.out.println("문제집 카운트 " + dto.getWorkbookCount());
    }

}
