package com.moonspoon.moonspoon.workbook;


import com.moonspoon.moonspoon.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    private EntityManager entityManager;

    private User user;
    @BeforeEach
    public void setUp(){
        User user = new User();
        user.setUsername("test1");
        user.setName("test1");

        this.user = user;
    }


    @Test
    @DisplayName("내 문제집 조회 성능 측정")
    public void testFindAllWorkbookPerformance(){
        //given
        for(int i = 0; i < 100000; i++){
            Workbook workbook = new Workbook();
            workbook.setTitle("test Title " + i);
            workbook.setUser(this.user);
        }

        // start time
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        service.findAll("", 1, 12, "test1");

        // end time
        //then
        long endTime = System.currentTimeMillis();

        System.out.println("조회시간 " + (endTime-startTime) + "ms");
    }

}
