package com.moonspoon.moonspoon.repository;

import com.moonspoon.moonspoon.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    //todo fetch join 적용
    @Query("select p from Problem p join fetch p.workbook w where w.id = :workbookId")
    List<Problem> findAllByWorkbookId(@Param("workbookId") Long workbookId);
}

