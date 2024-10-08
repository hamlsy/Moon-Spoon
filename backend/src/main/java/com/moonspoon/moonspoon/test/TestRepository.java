package com.moonspoon.moonspoon.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("select t from Test t " +
            "join fetch t.sharedWorkbook s " +
            "join fetch s.workbook w " +
            "join fetch w.problems where t.id = :id")
    Optional<Test> findByIdWithSharedWorkbookAndWorkbookAndProblems(@Param("id") Long id);

    @Query("select t from Test t " +
            "join fetch t.testAnswers a " +
            "join fetch a.problem " +
            "where t.id = :id")
    Optional<Test> findByIdWithTestAnswersAndProblem(@Param("id") Long id);

    @Query("select count(t) from Test t where t.user.username = :username")
    int countByUsername(@Param("username") String username);

}
