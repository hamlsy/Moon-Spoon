package com.moonspoon.moonspoon.core.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.id = :id")
    Optional<Comment> findByIdWithUser(@Param("id") Long id);

    @Query("select c from Comment c where c.sharedWorkbook.id = :id")
    List<Comment> findAllBySharedWorkbookId(@Param("id") Long id);
}
