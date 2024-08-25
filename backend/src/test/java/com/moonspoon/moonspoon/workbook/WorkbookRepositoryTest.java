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
    Page<Workbook> findAllVer1(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);


    @Query("SELECT w FROM Workbook w " +
            "JOIN fetch w.user u " +
            "WHERE u.username = :username and " +
            "(lower(w.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(w.content) like lower(concat('%',:keyword,'%')))")
    Page<Workbook> findAllVer2(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);

    @Query("select count(p) from Problem p where p.workbook.id = :workbookId")
    int countProblemsByWorkbookId(@Param("workbookId") Long workbookId);

    @Query(value = "SELECT w.* FROM Workbook w " +
            "WHERE w.user_id IN (SELECT user_id FROM users WHERE username = :username) " +
            "AND MATCH(w.title, w.content) AGAINST(:keyword IN NATURAL LANGUAGE MODE)",
            countQuery = "SELECT COUNT(*) FROM Workbook w " +
                    "WHERE w.user_id IN (SELECT user_id FROM users WHERE username = :username) " +
                    "AND MATCH(w.title, w.content) AGAINST(:keyword IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    Page<Workbook> findAllVer3(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);


    @Query("SELECT w FROM Workbook w " +
            "JOIN fetch w.user u " +
            "WHERE u.username = :username and " +
            "(lower(w.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(w.content) like lower(concat('%',:keyword,'%')))")
    Page<Workbook> findAllVer4(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);

}
