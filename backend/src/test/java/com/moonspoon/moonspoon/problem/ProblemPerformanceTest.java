package com.moonspoon.moonspoon.problem;

import com.moonspoon.moonspoon.core.testAnswer.TestAnswer;
import com.moonspoon.moonspoon.core.testAnswer.TestAnswerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
public class ProblemPerformanceTest {

    @Autowired
    private TestAnswerRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<TestAnswer> testAnswerList = new ArrayList<>();

    private Long beforeSize;
    @BeforeEach
    void setUp(){
        int count = 10000;
        beforeSize = repository.count();
        for(int i = 0 ; i < count ; i ++){
            TestAnswer testAnswer = new TestAnswer();
            testAnswer.setName("dd");
            testAnswer.setUserAnswer("answer"+i);
            this.testAnswerList.add(testAnswer);
        }
    }


    @Test
    @DisplayName("Problem with saveAll")
    void saveAllTest(){
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        List<TestAnswer> testAnswers = repository.saveAll(this.testAnswerList);

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("조회시간 " + (endTime-startTime) + "ms");

        Assertions.assertEquals(10000, testAnswers.size());
        Assertions.assertEquals(10000, testAnswers.get(9999).getId() - testAnswers.get(0).getId()+1);
    }

    @Test
    @DisplayName("Problem with Bulk Insert")
    void bulkInsertTest(){
        long startTime = System.currentTimeMillis();

        //when
        // 조회
        String sql = "INSERT INTO test_answer (name, user_answer) VALUE (?, ?)";
        jdbcTemplate.batchUpdate(
                sql, this.testAnswerList, this.testAnswerList.size(),
                (PreparedStatement ps, TestAnswer testAnswer) -> {
                    ps.setString(1, testAnswer.getName());
                    ps.setString(2, testAnswer.getUserAnswer());
                });

        //then
        long endTime = System.currentTimeMillis();
        System.out.println("조회시간 " + (endTime-startTime) + "ms");
        Long testAnswerSize = repository.count();
        System.out.println("전체 개수 " + (testAnswerSize-beforeSize));
    }

}
