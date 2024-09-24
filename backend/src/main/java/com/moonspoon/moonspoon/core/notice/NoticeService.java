package com.moonspoon.moonspoon.core.notice;

import com.moonspoon.moonspoon.api.notice.dto.request.NoticeCreateRequest;
import com.moonspoon.moonspoon.api.notice.dto.request.NoticeUpdateRequest;
import com.moonspoon.moonspoon.api.notice.dto.response.NoticeListResponse;
import com.moonspoon.moonspoon.api.notice.dto.response.NoticeResponse;
import com.moonspoon.moonspoon.common.exception.custom.AccessDeniedException;
import com.moonspoon.moonspoon.common.exception.custom.NotFoundException;
import com.moonspoon.moonspoon.core.user.User;
import com.moonspoon.moonspoon.core.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    private static final String ADMIN_ROLE = "admin";

    private static final String NOTICE_NOT_FOUND_MESSAGE = "존재하지 않는 글입니다.";
    private static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 유저입니다.";
    private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다.";

    public List<NoticeListResponse> findAllNotice(){
        List<Notice> notices = noticeRepository.findAllWithUserDesc();
        return notices.stream().map(NoticeListResponse::fromEntity)
                .collect(Collectors.toList());
    }


    public NoticeResponse findNotice(Long id){
        Notice notice = findByIdWithUser(id);
        return NoticeResponse.fromEntity(notice);
    }

    @Transactional
    @CacheEvict(value = "notices", allEntries = true)
    public NoticeResponse createNotice(NoticeCreateRequest request){
        User user = validateAdmin();
        Notice notice = NoticeCreateRequest.toEntity(request);
        notice.setCreateDate(LocalDateTime.now());
        notice.setAuthor(user.getName());
        notice.setUser(user);
        noticeRepository.save(notice);

        return NoticeResponse.fromEntity(notice);
    }

    @Transactional
    @CacheEvict(value = "notices", allEntries = true)
    public void deleteNotice(Long id){
        validateAdmin();
        noticeRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "notices", allEntries = true)
    public NoticeResponse updateNotice(Long id, NoticeUpdateRequest request){
        validateAdmin();
        Notice notice = findByIdWithUser(id);
        notice.update(request.getTitle(), request.getContent());
        notice.setUpdateDate(LocalDateTime.now());
        return NoticeResponse.fromEntity(notice);
    }

    private User validateAdmin(){
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND_MESSAGE)
        );
        if(user == null || !user.getRole().getValue().equals(ADMIN_ROLE)){
            throw new AccessDeniedException(UNAUTHORIZED_MESSAGE);
        }
        return user;
    }

    @Cacheable(value = "notices", keyGenerator = "customKeyGenerator")
    public List<NoticeListResponse> getRecentNotices(){
        Pageable pageable = PageRequest.of(0, 3);
        List<Notice> notices = noticeRepository.findRecentNotices(pageable);
        return notices.stream().map(NoticeListResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private Notice findByIdWithUser(Long id){
        return noticeRepository.findByIdWithUser(id).orElseThrow(
                () -> new NotFoundException(NOTICE_NOT_FOUND_MESSAGE));
    }

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
