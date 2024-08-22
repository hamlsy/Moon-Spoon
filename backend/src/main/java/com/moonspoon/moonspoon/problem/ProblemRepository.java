package com.moonspoon.moonspoon.problem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("select p from Problem p " +
            "where p.workbook.id = :workbookId")
    Page<Problem> findAllByWorkbookId(@Param("workbookId") Long workbookId, Pageable pageable);
}

