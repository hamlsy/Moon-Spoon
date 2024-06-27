package com.moonspoon.moonspoon.repository;

import com.moonspoon.moonspoon.domain.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
}
