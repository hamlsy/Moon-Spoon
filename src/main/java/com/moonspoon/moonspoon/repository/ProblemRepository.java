package com.moonspoon.moonspoon.repository;

import com.moonspoon.moonspoon.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    public List<Problem> findAllByWorkbookId(Long workbookId);
}

