package com.moonspoon.moonspoon.notice;


import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRepository;
import com.moonspoon.moonspoon.user.UserRole;
import com.moonspoon.moonspoon.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class NoticeServiceTest {
    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void createAdmin(){
        //given
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setName("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));
        adminUser.setRole(UserRole.ADMIN);

        //when
        userRepository.save(adminUser);

        //then
        User savedUser = userRepository.findByUsername("admin");

        assertNotNull(savedUser, "Saved user is null");
        assertEquals("admin", savedUser.getRole().getValue());
    }

    @Test
    @Transactional
    void createNoticeTest(){

    }
}
