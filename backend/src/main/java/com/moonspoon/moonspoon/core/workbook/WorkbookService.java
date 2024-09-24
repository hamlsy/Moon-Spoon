package com.moonspoon.moonspoon.core.workbook;

import com.moonspoon.moonspoon.api.workbook.dto.response.WorkbookProblemCountDto;
import com.moonspoon.moonspoon.core.user.User;
import com.moonspoon.moonspoon.api.workbook.dto.request.WorkbookCreateRequest;
import com.moonspoon.moonspoon.api.workbook.dto.request.WorkbookUpdateRequest;
import com.moonspoon.moonspoon.api.workbook.dto.response.WorkbookResponse;
import com.moonspoon.moonspoon.common.exception.custom.NotFoundException;
import com.moonspoon.moonspoon.common.exception.custom.NotUserException;
import com.moonspoon.moonspoon.core.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class WorkbookService {
    private static final String DEFAULT_SORT_FIELD = "createDate";
    private static final String OLDEST_ORDER = "oldest";
    private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다.";
    private static final String WORKBOOK_NOT_FOUND_MESSAGE = "존재하지 않는 문제집입니다.";

    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;

    //등록
    @Transactional
    @CacheEvict(value = "workbooks", allEntries = true)
    public WorkbookResponse createWorkbook(WorkbookCreateRequest dto) {
        User user = getAuthenticatedUser();
        Workbook workbook = createWorkbookEntity(dto, user);
        return WorkbookResponse.fromEntity(workbookRepository.save(workbook));
    }

    private Workbook createWorkbookEntity(WorkbookCreateRequest dto, User user){
        Workbook workbook = WorkbookCreateRequest.toEntity(dto);
        workbook.setAuthor(user.getName());
        workbook.setCreateDate(LocalDateTime.now());
        workbook.setUser(user);
        return workbook;
    }


    //단일 조회
    public WorkbookResponse findOneById(Long id){
        String username = getAuthenticatedUsername();
        Workbook workbook = findWorkbookById(id);
        validateWorkbookOwnership(workbook, username);
        return WorkbookResponse.fromEntity(workbook);
    }

    private void validateWorkbookOwnership(Workbook workbook, String username){
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException(UNAUTHORIZED_MESSAGE);
        }
    }

    @Cacheable(value = "workbooks", keyGenerator="customKeyGenerator")
    public Page<WorkbookResponse> findAll(String keyword, String order, int page, int size){
        String username = getAuthenticatedUsername();
        Pageable pageable = createPageable(order, page, size);

        Page<Workbook> workbooks = workbookRepository.findAllWithUserAndKeyword(keyword.trim(), pageable, username);
        Map<Long, Long> problemCountMap = getProblemCountMap(workbooks);

        Page<WorkbookResponse> responses = workbooks.map(w -> createWorkbookResponse(w, problemCountMap));

        return responses;
    }

    private Pageable createPageable(String order, int page, int size){
        Sort sort = OLDEST_ORDER.equals(order) ?
                Sort.by(DEFAULT_SORT_FIELD).ascending() :
                Sort.by(DEFAULT_SORT_FIELD).descending();
        return PageRequest.of(page, size, sort);
    }


    private Map<Long, Long> getProblemCountMap(Page<Workbook> workbooks){
        List<Long> workbooksIds = getWorkbookIds(workbooks);
        List<WorkbookProblemCountDto> problemCounts = workbookRepository.countProblemsByWorkbookIds(workbooksIds);
        return problemCounts.stream()
                .collect(Collectors.toMap(
                        WorkbookProblemCountDto::getWorkbookId,
                        WorkbookProblemCountDto::getProblemCount
                ));
    }

    private List<Long> getWorkbookIds(Page<Workbook> workbooks){
        return workbooks.stream()
                .map(Workbook::getId).collect(Collectors.toList());
    }

    private WorkbookResponse createWorkbookResponse(Workbook workbook, Map<Long, Long> problemCountMap){
        WorkbookResponse response =  WorkbookResponse.fromEntity(workbook);
        Long problemCount = problemCountMap.get(workbook.getId());
        int returnProblemCount = (problemCount != null) ? problemCount.intValue() : 0;
        response.setProblemCount(returnProblemCount);
        return response;
    }

    //수정
    @Transactional
    @CacheEvict(value = "workbooks", allEntries = true)
    public WorkbookResponse updateWorkbook(Long id, WorkbookUpdateRequest dto){
        Workbook workbook = findWorkbookById(id);
        validateWorkbookOwnership(workbook, getAuthenticatedUsername());
        workbook.update(dto.getTitle(), dto.getContent(), LocalDateTime.now());
        return WorkbookResponse.fromEntity(workbook);
    }


    //삭제
    @Transactional
    @CacheEvict(value = "workbooks", allEntries = true)
    public void deleteWorkbook(Long id){
        Workbook workbook = findWorkbookById(id);
        validateWorkbookOwnership(workbook, getAuthenticatedUsername());
        workbookRepository.delete(workbook);
    }

    private Workbook findWorkbookById(Long id){
        return workbookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(WORKBOOK_NOT_FOUND_MESSAGE));
    }

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String getAuthenticatedUsername(){
        String username = getCurrentUsername();
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException(UNAUTHORIZED_MESSAGE);
        }
        return username;
    }

    private User getAuthenticatedUser(){
        String username = getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new NotUserException(UNAUTHORIZED_MESSAGE)
                );
    }


}
