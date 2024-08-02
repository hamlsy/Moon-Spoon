package com.moonspoon.moonspoon.workbook;

import com.moonspoon.moonspoon.workbook.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    @Query("SELECT DISTINCT w FROM Workbook w " +
    "JOIN fetch w.user u " +
    "LEFT JOIN FETCH w.problems p " +
    "WHERE u.username = :username")
    List<Workbook> findWorkbookWithUserAndProblems(@Param("username") String username);
}
