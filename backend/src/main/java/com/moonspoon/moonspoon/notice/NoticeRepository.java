package com.moonspoon.moonspoon.notice;

import com.moonspoon.moonspoon.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
