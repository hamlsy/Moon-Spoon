package com.moonspoon.moonspoon.repository;

import com.moonspoon.moonspoon.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}

