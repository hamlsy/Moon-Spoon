package com.moonspoon.moonspoon.notice;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select n from Notice n " +
            "join fetch n.user u where n.id = :noticeId")
    Optional<Notice> findByIdWithUser(@Param("noticeId") Long noticeId);

    @Query("select distinct n from Notice n " +
            "join fetch n.user order by n.createDate desc")
    List<Notice> findAllWithUserDesc();

    @Query("select n from Notice n " +
    "order by n.createDate desc")
    List<Notice> findRecentNotices(Pageable pageable);


}
