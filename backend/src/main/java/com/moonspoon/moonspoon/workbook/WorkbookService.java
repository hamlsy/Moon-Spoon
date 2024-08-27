package com.moonspoon.moonspoon.workbook;

import com.moonspoon.moonspoon.dto.response.WorkbookProblemCountDto;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.dto.request.workbook.WorkbookCreateRequest;
import com.moonspoon.moonspoon.dto.request.workbook.WorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.WorkbookResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;

    //등록
    @Transactional
    @CacheEvict(value = "workbooks", allEntries = true)
    public WorkbookResponse createWorkbook(WorkbookCreateRequest dto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);
        User user = userRepository.findByUsername(username);

        Workbook workbook = WorkbookCreateRequest.toEntity(dto);
        workbook.setAuthor(user.getName());
        workbook.setCreateDate(LocalDateTime.now());
        workbook.setUser(user);

        workbookRepository.save(workbook);
        return WorkbookResponse.fromEntity(workbook);
    }

    private void validateUser(String username) {
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException("권한이 없습니다.");
        }
    }

    //단일 조회
    public WorkbookResponse findOneById(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);

        Workbook workbook = workbookRepository.findById(id).orElseThrow(
                () -> new NotFoundException("문제집이 존재하지 않습니다.")
        );
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }

        return WorkbookResponse.fromEntity(workbook);
    }


    @Cacheable(value = "workbooks", keyGenerator="customKeyGenerator")
    public Page<WorkbookResponse> findAll(String keyword, String order, int page, int size){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);
        Sort sort = Sort.by("createDate").descending();
        if (order.equals("oldest")){
            sort = Sort.by("createDate").ascending();
        }
        Pageable pageable = PageRequest.of(page, size,  sort);

        Page<Workbook> workbooks = workbookRepository.findAllWithUserAndKeyword(keyword.trim(), pageable, username);

        List<Long> workbookIds = workbooks.stream()
                .map(Workbook::getId).collect(Collectors.toList());
        List<WorkbookProblemCountDto> problemCounts = workbookRepository.countProblemsByWorkbookId(workbookIds);
        Map<Long, Long> problemCountMap = problemCounts.stream().collect(Collectors.toMap(
                WorkbookProblemCountDto::getWorkbookId, WorkbookProblemCountDto::getProblemCount
                ));

        Page<WorkbookResponse> responses = workbooks.map(w -> {
            WorkbookResponse response =  WorkbookResponse.fromEntity(w);
            Long problemCount = problemCountMap.get(w.getId());
            int returnProblemCount = (problemCount != null) ? problemCount.intValue() : 0;
            response.setProblemCount(returnProblemCount);
            return response;
        });

        return responses;
    }


    //수정
    @Transactional
    @CacheEvict(value = "workbooks", allEntries = true)
    public WorkbookResponse updateWorkbook(Long id, WorkbookUpdateRequest dto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);

        Workbook workbook = workbookRepository.findById(id).orElseThrow();
        LocalDateTime currentTime = LocalDateTime.now();
        workbook.update(dto.getTitle(), dto.getContent(), currentTime);
        return WorkbookResponse.fromEntity(workbook);
    }

    //삭제
    @Transactional
    @CacheEvict(value = "workbooks", allEntries = true)
    public void deleteWorkbook(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);

        if(!workbookRepository.existsById(id)){
            new NotFoundException("문제집이 존재하지 않습니다.");
        }
        workbookRepository.deleteById(id);
    }

}
