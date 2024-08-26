package com.moonspoon.moonspoon.workbook;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkbookRepositoryTest extends JpaRepository<Workbook, Long> {
    @Query("SELECT DISTINCT w FROM Workbook w " +
            "JOIN fetch w.user u " +
            "LEFT JOIN FETCH w.problems p " +
            "WHERE u.username = :username and " +
            "(lower(w.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(w.content) like lower(concat('%',:keyword,'%')))")
    Page<Workbook> findAllVer1(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);


    @Query("SELECT w FROM Workbook w " +
            "JOIN w.user u " +
            "where u.username = :username and " +
            "(lower(w.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(w.content) like lower(concat('%',:keyword,'%')))")
    Page<Workbook> findAllVer2(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);

    @Query("select new com.moonspoon.moonspoon.workbook.WorkbookProblemCountTestDto(p.workbook.id, COUNT(p)) " +
            "from Problem p where p.workbook.id in :workbookIds " +
            "group by p.workbook.id")
    List<WorkbookProblemCountTestDto> countProblemsByWorkbookId(@Param("workbookIds") List<Long> workbookIds);

    @Query(value = "SELECT distinct w.* FROM Workbook w " +
            "WHERE w.user_id IN (SELECT user_id FROM users WHERE username = :username) " +
            "AND MATCH(w.title, w.content) AGAINST(:keyword IN BOOLEAN MODE)",
            countQuery = "SELECT COUNT(*) FROM Workbook w " +
                    "WHERE w.user_id IN (SELECT user_id FROM users WHERE username = :username) " +
                    "AND MATCH(w.title, w.content) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true)
    Page<Workbook> findAllVer3(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);


    @Query("SELECT w FROM Workbook w " +
            "JOIN fetch w.user u " +
            "WHERE u.username = :username and " +
            "(lower(w.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(w.content) like lower(concat('%',:keyword,'%')))")
    Page<Workbook> findAllVer4(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);

}
