package com.moonspoon.moonspoon.user;

import com.moonspoon.moonspoon.dto.response.user.UserProfileResponse;
import com.moonspoon.moonspoon.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    User findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);

    boolean existsByName(String name);

    @Query("select u from User u " +
            "join fetch u.tests " +
            "where u.username = :username ")
    User findByUsernameWithTest(@Param("username") String username);

    @Query("select new com.moonspoon.moonspoon.dto.response.user.UserProfileResponse(" +
            "count(distinct w), count(distinct s), count(distinct t)) from User u " +
            "left join u.workbooks w left join u.sharedWorkbooks s left join u.tests t " +
            "where u.id = :id")
    UserProfileResponse userProfileCountById(@Param("id") Long id);

}
