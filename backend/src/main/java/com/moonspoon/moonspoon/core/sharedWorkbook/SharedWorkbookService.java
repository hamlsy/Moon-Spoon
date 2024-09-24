package com.moonspoon.moonspoon.core.sharedWorkbook;

import com.moonspoon.moonspoon.core.comment.CommentRepository;
import com.moonspoon.moonspoon.api.sharedWorkbook.dto.request.SharedWorkbookRequest;
import com.moonspoon.moonspoon.api.sharedWorkbook.dto.request.SharedWorkbookUpdateRequest;
import com.moonspoon.moonspoon.api.comment.dto.response.CommentResponse;
import com.moonspoon.moonspoon.api.sharedWorkbook.dto.response.SharedWorkbookListResponse;
import com.moonspoon.moonspoon.api.sharedWorkbook.dto.response.SharedWorkbookResponse;
import com.moonspoon.moonspoon.core.workbook.Workbook;
import com.moonspoon.moonspoon.core.workbook.WorkbookRepository;
import com.moonspoon.moonspoon.common.exception.custom.NotFoundException;
import com.moonspoon.moonspoon.common.exception.custom.NotUserException;
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

    private static final String WORKBOOK_NOT_FOUND_MESSAGE = "존재하지 않는 문제집입니다.";
    private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다.";

    //단일 조회 + 댓글 조회
    public SharedWorkbookResponse findSharedWorkbook(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithWorkbookAndUser(id).orElseThrow(
                () -> new NotFoundException(WORKBOOK_NOT_FOUND_MESSAGE)
        );
        int problemCount = workbookRepository.countProblemsById(sharedWorkbook.getWorkbook().getId()).intValue();
        SharedWorkbookResponse response = SharedWorkbookResponse.fromEntity(sharedWorkbook);
        response.setProblemCount(problemCount);
        response.setUser(getUserOwnerShip(sharedWorkbook, getCurrentUsername()));
        response.setComments(getComments(id));
        return response;
    }

    private List<CommentResponse> getComments(Long sharedWorkbookId){
        return commentRepository.findAllBySharedWorkbookId(sharedWorkbookId).stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //전체 조회
    public Page<SharedWorkbookListResponse> findAllSharedWorkbook(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<SharedWorkbook> sharedWorkbooks = sharedWorkbookRepository.findAllWithKeyword(keyword.trim(), pageable);
        return sharedWorkbooks.map(SharedWorkbookListResponse::fromEntity);
    }

    //등록
    @Transactional
    public SharedWorkbookResponse sharedWorkbook(SharedWorkbookRequest dto){
        SharedWorkbook sharedWorkbook = createSharedWorkbook(dto);
        sharedWorkbookRepository.save(sharedWorkbook);

        return SharedWorkbookResponse.fromEntity(sharedWorkbook);
    }

    private SharedWorkbook createSharedWorkbook(SharedWorkbookRequest dto){
        SharedWorkbook sharedWorkbook = SharedWorkbookRequest.toEntity(dto);
        Workbook workbook = workbookRepository.findByIdWithUser(dto.getWorkbookId()).orElseThrow(
                () -> new NotFoundException(WORKBOOK_NOT_FOUND_MESSAGE)
        );
        sharedWorkbook.setCreateDate(LocalDateTime.now());
        sharedWorkbook.setWorkbook(workbook);
        sharedWorkbook.setAuthor(workbook.getAuthor());
        sharedWorkbook.setUser(workbook.getUser());
        sharedWorkbook.setRandom(dto.isRandom());
        return sharedWorkbook;
    }

    //수정
    @Transactional
    public SharedWorkbookResponse updateSharedWorkbook(Long id, SharedWorkbookUpdateRequest dto){
        SharedWorkbook sharedWorkbook = findSharedWorkbookByIdWithUser(id);
        validatedSharedWorkbookOwnerShip(sharedWorkbook, getCurrentUsername());

        sharedWorkbook.updateSharedWorkbook(dto.getTitle(), dto.getContent(),
                dto.isRandom(), LocalDateTime.now());

        return SharedWorkbookResponse.fromEntity(sharedWorkbook);
    }

    private SharedWorkbook findSharedWorkbookByIdWithUser(Long id){
        return sharedWorkbookRepository.findByIdWithUser(id)
                .orElseThrow(
                        () -> new NotFoundException(WORKBOOK_NOT_FOUND_MESSAGE)
                );
    }

    //삭제
    @Transactional
    public void deleteSharedWorkbook(Long id){
        SharedWorkbook sharedWorkbook = findSharedWorkbookByIdWithUser(id);
        validatedSharedWorkbookOwnerShip(sharedWorkbook, getCurrentUsername());
        sharedWorkbookRepository.delete(sharedWorkbook);
    }

    private void validatedSharedWorkbookOwnerShip(SharedWorkbook sharedWorkbook, String username){
        if(!sharedWorkbook.getUser().getUsername().equals(username)){
            throw new NotUserException(UNAUTHORIZED_MESSAGE);
        }
    }

    //유저 검증
    private boolean getUserOwnerShip(SharedWorkbook sharedWorkbook, String username){
        return sharedWorkbook.getUser().getUsername().equals(username) ? true : false;
    }

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
