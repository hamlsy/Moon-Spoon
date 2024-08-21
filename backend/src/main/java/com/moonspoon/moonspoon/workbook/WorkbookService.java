package com.moonspoon.moonspoon.workbook;

import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.dto.request.workbook.WorkbookCreateRequest;
import com.moonspoon.moonspoon.dto.request.workbook.WorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.WorkbookResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.user.UserRepository;

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
public class WorkbookService {
    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;

    @Transactional
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

    public Page<WorkbookResponse> findAll(int page, int size){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);

        Pageable pageable = PageRequest.of(page, size,  Sort.by("createDate").descending());

        Page<Workbook> workbooks = workbookRepository.findAllWithUserAndProblems(pageable, username);
        if(workbooks.isEmpty()){
            throw new NotFoundException("문제집이 존재하지 않습니다.");
        }
        Page<WorkbookResponse> responses = workbooks.map(WorkbookResponse::fromEntity);
        return responses;
    }

    //수정
    @Transactional
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
    public void deleteWorkbook(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);

        if(!workbookRepository.existsById(id)){
            new NotFoundException("문제집이 존재하지 않습니다.");
        }

        workbookRepository.deleteById(id);
    }



}
