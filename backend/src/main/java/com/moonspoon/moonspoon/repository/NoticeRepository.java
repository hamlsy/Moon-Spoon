package com.moonspoon.moonspoon.repository;

import com.moonspoon.moonspoon.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
