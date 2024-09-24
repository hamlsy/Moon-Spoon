package com.moonspoon.moonspoon.common.init;

import com.moonspoon.moonspoon.core.problem.Problem;
import com.moonspoon.moonspoon.core.user.User;
import com.moonspoon.moonspoon.core.user.UserRole;
import com.moonspoon.moonspoon.core.workbook.Workbook;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init(){
//        initService.dbInit1();
//        initService.dbInit2();
//        initService.dbInitLargeTestData();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService{
        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;
        public void dbInit1(){
            User user = new User();
            user.setUsername("dd");
            user.setName("dd");
            user.setPassword(passwordEncoder.encode("dd"));
            user.setRole(UserRole.USER);
            em.persist(user);

            for(int i = 1 ; i <= 3 ; i ++){
                Workbook w1 = new Workbook();
                w1.setTitle("w title" + i);
                w1.setContent("w content" + i);
                w1.setAuthor(user.getName());
                w1.setCreateDate(LocalDateTime.now());
                w1.setUser(user);
                for(int j = 1; j <= 10; j++){
                    Problem p = new Problem();
                    p.setQuestion("문제" + j);
                    p.setSolution("정답" + j);
                    p.setCorrectRate(0.11 * j);
                    p.setCreateDate(LocalDateTime.now().plusMinutes(j*10));
                    p.setWorkbook(w1);
                    em.persist(p);
                }
                em.persist(w1);
            }

        }
        public void dbInit2(){
            User user = new User();
            user.setUsername("aa");
            user.setName("aa");
            user.setPassword(passwordEncoder.encode("aa"));
            user.setRole(UserRole.USER);
            em.persist(user);

            Workbook w = new Workbook();
            w.setTitle("aa workbook");
            w.setContent("aa content");
            w.setAuthor(user.getName());
            w.setCreateDate(LocalDateTime.now());
            w.setUser(user);
            em.persist(w);

            Problem p = new Problem();
            p.setQuestion("a question");
            p.setSolution("a solution");
            p.setCreateDate(LocalDateTime.now());
            p.setWorkbook(w);
            em.persist(p);


        }

        public void dbInitLargeTestData(){
            User user  = new User();
            user.setUsername("testUser");
            user.setName("testUser");
            user.setRole(UserRole.USER);

            em.persist(user);

            int batchSize = 1000;
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
                    em.persist(problem);
                }

                if (i % batchSize == 0) {
                    em.flush();
                    em.clear();
                }
            }

        }
    }
}
