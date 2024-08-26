package com.moonspoon.moonspoon.workbook;



import com.moonspoon.moonspoon.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
//        this.user = new User();
//        int count = 16;
//        user.setUsername("test"+count);
//        user.setName("test"+count);
//        user.setRole(UserRole.USER);
//
//        em.persist(user);
//
//        int batchSize = 1000;
//        int totalWorkbooks = 100;
//        int totalProblems = 1000;
//        for (int i = 0; i < totalWorkbooks; i++) {
//            Workbook workbook = new Workbook();
//            workbook.setTitle("test Title " + i);
//            workbook.setContent("test Content " + i);
//            workbook.setUser(user);
//            workbook.setCreateDate(LocalDateTime.now());
//
//            em.persist(workbook);
//
//            for (int j = 0; j < totalProblems; j++) {
//                Problem problem = new Problem();
//                problem.setQuestion("test Question " + j);
//                problem.setWorkbook(workbook);
//                em.persist(problem);
//            }
//
//            if (i % batchSize == 0) {
//                em.flush();
//                em.clear();
//            }
//        }

    }

    @Test
    @DisplayName("내 문제집 조회 성능 측정 ver 1")
    public void findAllTestVer1(){
        //given
        String username = "testUser";
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        Page<Workbook> workbooks = service.findAllVer1("test Title 24", 0, 12, username);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("조회시간 " + (endTime-startTime) + "ms");

        Assertions.assertEquals(1, workbooks.getTotalElements());
        Assertions.assertEquals(1000, workbooks.getContent().get(0).getProblemCount());
    }


    @Test
    @DisplayName("내 문제집 조회 성능 측정 ver 2")
    public void findAllTestVer2(){
        //given
        String username = "testUser";
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        Page<Workbook> workbooks = service.findAllVer2("test Title 24", 0, 12, username);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("조회시간 " + (endTime-startTime) + "ms");

        Assertions.assertEquals(1, workbooks.getTotalElements());
        Assertions.assertEquals(1000, workbooks.getContent().get(0).getProblemCount());
    }

    @Test
    @DisplayName("내 문제집 조회 성능 측정 ver 3, FULL TEXT 설정")
    public void findAllTestVer3(){
        //given
//        em.createNativeQuery("alter table Workbook drop index idx_fulltext").executeUpdate();
        em.createNativeQuery("alter table Workbook add fulltext index idx_fulltext (title, content)").executeUpdate();

        String username = "testUser";
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        Page<Workbook> workbooks = service.findAllVer3("\"test Title 24\"", 0, 12, username);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("---------조회시간 " + (endTime-startTime) + "ms--------");
        em.createNativeQuery("alter table Workbook drop index idx_fulltext").executeUpdate();

        Assertions.assertEquals(1, workbooks.getTotalElements());
        Assertions.assertEquals(1000, workbooks.getContent().get(0).getProblemCount());
    }

    @Test
    @DisplayName("내 문제집 조회 성능 측정 ver 4, 캐싱")
    public void findAllTestVer4(){
        //given
        String username = "testUser";
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        Page<Workbook> workbooks = service.findAllVer4("test Title 24", 0, 12, username);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("Ver4 ---------조회시간 " + (endTime-startTime) + "ms--------");

        Assertions.assertEquals(1, workbooks.getTotalElements());
        Assertions.assertEquals(1000, workbooks.getContent().get(0).getProblemCount());

    }

    @AfterEach
    public void end(){
        //delete data
    }
}
