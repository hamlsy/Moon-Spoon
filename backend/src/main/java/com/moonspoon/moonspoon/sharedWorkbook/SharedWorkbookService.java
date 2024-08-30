package com.moonspoon.moonspoon.sharedWorkbook;

import com.moonspoon.moonspoon.comment.CommentRepository;
import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookRequest;
import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.comment.CommentResponse;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookListResponse;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.workbook.WorkbookRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharedWorkbookService {
    private final SharedWorkbookRepository sharedWorkbookRepository;
    private final WorkbookRepository workbookRepository;
    private final CommentRepository commentRepository;
    private final String notFoundWorkbookMessage = "존재하지 않는 문제집입니다.";


    //단일 조회 + 댓글 조회
    public SharedWorkbookResponse findSharedWorkbook(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithWorkbook(id).orElseThrow(
                () -> new NotFoundException(notFoundWorkbookMessage)
        );
        int problemCount = workbookRepository.countProblemsById(sharedWorkbook.getWorkbook().getId()).intValue();
        SharedWorkbookResponse response = SharedWorkbookResponse.fromEntity(sharedWorkbook);
        response.setProblemCount(problemCount);
        response.setUser(getUser(sharedWorkbook));
        List<CommentResponse> comments = commentRepository.findAllBySharedWorkbookId(id).stream()
                        .map(c -> CommentResponse.fromEntity(c))
                                .collect(Collectors.toList());
        response.setComments(comments);
        return response;
    }

    //전체 조회
    public Page<SharedWorkbookListResponse> findAllSharedWorkbook(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<SharedWorkbook> sharedWorkbooks = sharedWorkbookRepository.findAllWithKeyword(keyword.trim(), pageable);
        Page<SharedWorkbookListResponse> responses = sharedWorkbooks.map(SharedWorkbookListResponse::fromEntity);
        return responses;
    }


    //등록
    @Transactional
    public SharedWorkbookResponse sharedWorkbook(SharedWorkbookRequest dto){
        SharedWorkbook sharedWorkbook = SharedWorkbookRequest.toEntity(dto);
        Workbook workbook = workbookRepository.findByIdWithUser(dto.getWorkbookId()).orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                );
        sharedWorkbook.setCreateDate(LocalDateTime.now());
        sharedWorkbook.setWorkbook(workbook);
        sharedWorkbook.setAuthor(workbook.getAuthor());
        sharedWorkbook.setUser(workbook.getUser());

        sharedWorkbookRepository.save(sharedWorkbook);

        SharedWorkbookResponse response = SharedWorkbookResponse.fromEntity(sharedWorkbook);
        return response;
    }

    //수정
    @Transactional
    public SharedWorkbookResponse updateSharedWorkbook(Long id, SharedWorkbookUpdateRequest dto){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithUser(id)
                .orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                );
        validUser(sharedWorkbook.getUser().getUsername());

        sharedWorkbook.updateSharedWorkbook(dto.getTitle(), dto.getContent(),
                dto.isRandom(), LocalDateTime.now());

        SharedWorkbookResponse response = SharedWorkbookResponse.fromEntity(sharedWorkbook);
        return response;
    }


    //삭제
    @Transactional
    public void deleteSharedWorkbook(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithUser(id)
                .orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                );
        validUser(sharedWorkbook.getUser().getUsername());
        sharedWorkbookRepository.deleteById(id);
    }


    private void validUser(String username){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!currentUser.equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }
    }


    //유저 검증
    private boolean getUser(SharedWorkbook sharedWorkbook){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(currentUser.equals(sharedWorkbook.getUser().getUsername())){
            return true;
        }else{
            return false;
        }
    }

}
