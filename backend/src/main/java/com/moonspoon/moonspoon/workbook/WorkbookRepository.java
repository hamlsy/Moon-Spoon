package com.moonspoon.moonspoon.workbook;

import com.moonspoon.moonspoon.dto.response.WorkbookProblemCountDto;
import com.moonspoon.moonspoon.workbook.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface WorkbookRepository extends JpaRepository<Workbook, Long> {

    @Query("SELECT w FROM Workbook w " +
            "JOIN fetch w.user u " +
            "WHERE u.username = :username and " +
            "(lower(w.title) like lower(concat('%',:keyword,'%')) or " +
            "lower(w.content) like lower(concat('%',:keyword,'%')))")
    Page<Workbook> findAllWithUserAndKeyword(@Param("keyword") String keyword, Pageable pageable, @Param("username") String username);

    @Query("select new com.moonspoon.moonspoon.dto.response.WorkbookProblemCountDto(p.workbook.id, COUNT(p)) " +
            "from Problem p where p.workbook.id in :workbookIds " +
            "group by p.workbook.id")
    List<WorkbookProblemCountDto> countProblemsByWorkbookIds(@Param("workbookIds") List<Long> workbookIds);


    @Query("select count(p) from Problem p where p.workbook.id = :id")
    Long countProblemsById(@Param("id") Long id);

    @Query("select w from Workbook w join fetch w.user u where w.id = :id")
    Optional<Workbook> findByIdWithUser(@Param("id") Long id);

    @Query("select count(w) from Workbook w where w.user.username = :username ")
    int countByUsername(@Param("username") String username);

}
