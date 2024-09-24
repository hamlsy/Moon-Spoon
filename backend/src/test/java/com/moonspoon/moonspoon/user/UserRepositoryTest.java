package com.moonspoon.moonspoon.user;


import com.moonspoon.moonspoon.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryTest extends JpaRepository<User, Long> {


    @Query("select count(t) from Test t " +
            "where t.user.id = :id ")
    Long testCountByUserId(@Param("id") Long id);

    @Query("select count(s) from SharedWorkbook s " +
            "where s.workbook.user.id = :id ")
    Long sharedWorkbookCountByUserId(@Param("id") Long id);

    @Query("select count(w) from Workbook w " +
            "where w.user.id = :id ")
    Long workbookCountByUserId(@Param("id") Long id);


    @Query("select new com.moonspoon.moonspoon.user.UserInfoDtoTest(" +
            "count(distinct t), count(distinct s), count(distinct w)) from User u " +
            "left join u.tests t left join u.sharedWorkbooks s left join u.workbooks w " +
            "where u.id = :id")
    UserInfoDtoTest userInfoCountByUserId(@Param("id") Long id);
}
