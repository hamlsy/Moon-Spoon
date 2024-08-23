package com.moonspoon.moonspoon.workbook;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkbookRepositoryTest extends JpaRepository<Workbook, Long> {
    @Query("SELECT DISTINCT w FROM Workbook w " +
            "JOIN fetch w.user u " +
            "LEFT JOIN FETCH w.problems p " +
            "WHERE u.username = :username and " +
            "(lower(w.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(w.content) like lower(concat('%',:keyword,'%')))")
    Page<Workbook> findAllWithUserAndProblemsAndKeyword(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);
}
