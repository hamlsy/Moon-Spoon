package com.moonspoon.moonspoon.service;

import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.dto.request.WorkbookCreateRequest;
import com.moonspoon.moonspoon.dto.request.WorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.WorkbookResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.repository.UserRepository;
import com.moonspoon.moonspoon.repository.WorkbookRepository;

import lombok.RequiredArgsConstructor;
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
        if(workbook.getUser().getUsername() != username){
            throw new NotUserException("권한이 없습니다.");
        }

        return WorkbookResponse.fromEntity(workbook);
    }

    public List<WorkbookResponse> findAll(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);
        User user = userRepository.findByUsername(username);
        List<Workbook> workbooks = user.getWorkbooks();
        if(workbooks.isEmpty()){
            throw new NotFoundException("문제집이 존재하지 않습니다.");
        }
        return workbooks.stream()
                .map(w -> WorkbookResponse.fromEntity(w))
                .collect(Collectors.toList());
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
