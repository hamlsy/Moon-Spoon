package com.moonspoon.moonspoon.repository;

import com.moonspoon.moonspoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    User findByUsername(String username);
}
