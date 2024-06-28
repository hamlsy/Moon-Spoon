package com.moonspoon.moonspoon.init;

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

                em.persist(w1);
            }

        }
    }
}
