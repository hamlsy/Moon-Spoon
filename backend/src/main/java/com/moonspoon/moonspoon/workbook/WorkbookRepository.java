package com.moonspoon.moonspoon.workbook;

import com.moonspoon.moonspoon.workbook.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    @Query("SELECT DISTINCT w FROM Workbook w " +
    "JOIN fetch w.user u " +
    "LEFT JOIN FETCH w.problems p " +
    "WHERE u.username = :username")
    Page<Workbook> findAllWithUserAndProblems(Pageable pageable, @Param("username") String username);

    @Query("select w from Workbook w join fetch w.user u where w.id = :id")
    Optional<Workbook> findByIdWithUser(@Param("id") Long id);

    @Query("select count(w) from Workbook w where w.user.username = :username ")
    int countByUsername(@Param("username") String username);

}
