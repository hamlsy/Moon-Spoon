package com.moonspoon.moonspoon.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemRepositoryTest extends JpaRepository<Problem, Long> {


}
