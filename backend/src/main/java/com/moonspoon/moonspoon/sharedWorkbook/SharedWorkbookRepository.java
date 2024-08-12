package com.moonspoon.moonspoon.sharedWorkbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SharedWorkbookRepository extends JpaRepository<SharedWorkbook, Long> {

    @Query("select s from SharedWorkbook s join fetch s.user where s.id = :id")
    Optional<SharedWorkbook> findByIdWithUser(@Param("id") Long id);

    @Query("select s from SharedWorkbook s join fetch s.comments where s.id = :id")
    Optional<SharedWorkbook> findByIdWithComments(@Param("id") Long id);

    @Query("select s from SharedWorkbook s join fetch s.workbook w join fetch w.problems where s.id = :id")
    Optional<SharedWorkbook> findByIdWithWorkbookAndProblems(@Param("id") Long id);

}
