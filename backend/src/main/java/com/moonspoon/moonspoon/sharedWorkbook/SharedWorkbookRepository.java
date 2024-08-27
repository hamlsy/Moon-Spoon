package com.moonspoon.moonspoon.sharedWorkbook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SharedWorkbookRepository extends JpaRepository<SharedWorkbook, Long> {

    @Query("select s from SharedWorkbook s join fetch s.user where s.id = :id")
    Optional<SharedWorkbook> findByIdWithUser(@Param("id") Long id);

    @Query("select s from SharedWorkbook s join fetch s.comments where s.id = :id")
    Optional<SharedWorkbook> findByIdWithComments(@Param("id") Long id);

    @Query("select s from SharedWorkbook s " +
            "join fetch s.workbook w " +
            "where s.id = :id")
    Optional<SharedWorkbook> findByIdWithWorkbook(@Param("id") Long id);

    @Query("select count(s) from SharedWorkbook s where s.user.username = :username")
    int countByUsername(@Param("username") String username);

    @Query("select s from SharedWorkbook s " +
            "where lower(s.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(s.content) like lower(concat('%', :keyword, '%'))")
    Page<SharedWorkbook> findAllWithKeyword(@Param("keyword") String keyword, Pageable pageable);
}
