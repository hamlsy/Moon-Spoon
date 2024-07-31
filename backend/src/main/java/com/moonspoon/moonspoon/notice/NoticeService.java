package com.moonspoon.moonspoon.notice;

import com.moonspoon.moonspoon.dto.request.notice.NoticeCreateRequest;
import com.moonspoon.moonspoon.dto.request.notice.NoticeUpdateRequest;
import com.moonspoon.moonspoon.dto.response.notice.NoticeListResponse;
import com.moonspoon.moonspoon.dto.response.notice.NoticeResponse;
import com.moonspoon.moonspoon.exception.AccessDeniedException;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    public List<NoticeListResponse> findAllNotice(){
        List<Notice> notices = noticeRepository.findAll();
        List<NoticeListResponse> responses = notices.stream()
                .map(n -> NoticeListResponse.fromEntity(n))
                .collect(Collectors.toList());
        return responses;
    }

    public NoticeResponse findNotice(Long id){
        Notice notice = noticeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 글입니다."));
        return NoticeResponse.fromEntity(notice);
    }

    @Transactional
    public NoticeResponse createNotice(NoticeCreateRequest request){
        User user = validAdmin();
        Notice notice = NoticeCreateRequest.toEntity(request);
        notice.setAuthor(user.getName());
        notice.setUser(user);
        noticeRepository.save(notice);

        return NoticeResponse.fromEntity(notice);
    }

    @Transactional
    public void deleteNotice(Long id){
        validAdmin();
        noticeRepository.deleteById(id);
    }

    @Transactional
    public NoticeResponse updateNotice(Long id, NoticeUpdateRequest request){
        validAdmin();
        Notice notice = noticeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 글입니다."));
        notice.update(request.getTitle(), request.getContent());
        return NoticeResponse.fromEntity(notice);
    }

    private User validAdmin(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        if(user == null){
            throw new NotUserException("로그인이 필요한 서비스입니다.");
        }

        if(!user.getRole().getValue().equals("admin")){
            throw new AccessDeniedException("권한이 부족합니다.");
        }
        return user;
    }
}
