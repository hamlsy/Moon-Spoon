package com.moonspoon.moonspoon.core.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {


    @Query("select t from Test t " +
            "join fetch t.testAnswers a " +
            "join fetch a.problem " +
            "where t.id = :id")
    Optional<Test> findByIdWithTestAnswersAndProblem(@Param("id") Long id);


}
