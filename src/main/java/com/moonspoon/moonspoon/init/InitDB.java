package com.moonspoon.moonspoon.init;

import com.moonspoon.moonspoon.domain.Problem;
import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.domain.UserRole;
import com.moonspoon.moonspoon.domain.Workbook;
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
        initService.dbInit1();
        initService.dbInit2();
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
                    p.setCreateDate(LocalDateTime.now());
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
    }
}
