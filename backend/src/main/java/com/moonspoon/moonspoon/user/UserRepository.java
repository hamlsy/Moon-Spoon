package com.moonspoon.moonspoon.user;

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
            "join fetch u.workbooks w " +
            "join fetch u.sharedWorkbooks s " +
            "join fetch u.tests " +
            "where u.username = :username ")
    User findByUsernameWithWorkbookAndSharedWorkbookAndTest(@Param("username") String username);


}
