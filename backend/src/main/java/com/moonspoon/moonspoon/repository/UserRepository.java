package com.moonspoon.moonspoon.repository;

import com.moonspoon.moonspoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    User findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);

    boolean existsByName(String name);
}
