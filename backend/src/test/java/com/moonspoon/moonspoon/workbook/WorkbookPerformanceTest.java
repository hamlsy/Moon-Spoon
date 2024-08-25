package com.moonspoon.moonspoon.workbook;


import com.moonspoon.moonspoon.problem.Problem;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
public class WorkbookPerformanceTest {

    @Autowired
    private WorkbookService workbookService;

    @Autowired
    private WorkbookPerformanceService service;

    @Autowired
    private EntityManager em;

    private User user;
    @BeforeEach
    public void setUp() {
//        em.createNativeQuery("CREATE INDEX idx_username ON Users(username)").executeUpdate();
        this.user = new User();
        user.setUsername("test5");
        user.setName("test5");
        user.setRole(UserRole.USER);

        em.persist(user);

        int batchSize = 100;
        int totalWorkbooks = 100;
        int totalProblems = 1000;

        for (int i = 0; i < totalWorkbooks; i++) {
            Workbook workbook = new Workbook();
            workbook.setTitle("test Title " + i);
            workbook.setContent("test Content " + i);
            workbook.setUser(user);
            workbook.setCreateDate(LocalDateTime.now());

            em.persist(workbook);

            for (int j = 0; j < totalProblems; j++) {
                Problem problem = new Problem();
                problem.setQuestion("test Question " + j);
                problem.setWorkbook(workbook);
//                em.persist(problem);
            }

            if (i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

    }

    @Test
    @DisplayName("내 문제집 조회 성능 측정 ver 1")
    public void findAllTestVer1(){
        //given

        // start time
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        service.findAllVer1("", 1, 12, "test1");

        // end time
        //then
        long endTime = System.currentTimeMillis();

        System.out.println("조회시간 " + (endTime-startTime) + "ms");
    }


    @Test
    @DisplayName("내 문제집 조회 성능 측정 ver 2")
    public void findAllTestVer2(){
        //given

        // start time
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        service.findAllVer2("", 1, 12, "test1");

        // end time
        //then
        long endTime = System.currentTimeMillis();

        System.out.println("조회시간 " + (endTime-startTime) + "ms");

    }

    @AfterEach
    public void end(){
//        em.createNativeQuery("drop index idx_username on Users").executeUpdate();
    }
}
