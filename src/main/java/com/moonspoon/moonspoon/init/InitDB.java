package com.moonspoon.moonspoon.init;

import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.domain.UserRole;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        }
    }
}
