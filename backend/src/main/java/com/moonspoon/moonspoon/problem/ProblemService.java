package com.moonspoon.moonspoon.problem;

import com.moonspoon.moonspoon.dto.response.problem.ProblemAllResponse;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRepository;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.response.problem.ProblemCreateResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultSubmitResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.exception.ProblemNotInWorkbook;
import com.moonspoon.moonspoon.workbook.WorkbookRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private Map<String, List<TestResultRequest>> storedLists = new ConcurrentHashMap<>();

    @Transactional
    public ProblemCreateResponse create(Long workbookId, ProblemCreateRequest dto){
        Workbook workbook = validateUserAndWorkbook(workbookId);

        Problem problem = ProblemCreateRequest.toEntity(dto);

        problem.setWorkbook(workbook);

        problemRepository.save(problem);
        return ProblemCreateResponse.fromEntity(problem);
    }
    private Workbook validateUserAndWorkbook(Long workbookId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException("권한이 없습니다.");
        }
        //문제집 예외
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        //사용자 예외
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }
        return workbook;
    }

    public ProblemAllResponse findAll(Long workbookId, String keyword, String order, int page, int size){
        Workbook workbook = validateUserAndWorkbook(workbookId);
        Sort sort = Sort.by("createDate").descending();

        switch (order){
            case "oldest":
                sort = Sort.by("createDate").ascending();
                break;
            case "correctRateAsc":
                sort = Sort.by("correctRate").ascending();
                break;
            case "correctRateDesc":
                sort = Sort.by("correctRate").descending();
                break;
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Problem> problems = problemRepository.findAllByWorkbookIdAndKeyword(workbookId, keyword.trim(), pageable);
        Page<ProblemResponse> responses = problems.map(ProblemResponse::fromEntity);

        ProblemAllResponse response = new ProblemAllResponse();
        response.setWorkbookTitle(workbook.getTitle());
        response.setProblems(responses);
        return response;
    }


    @Transactional
    public ProblemResponse update(Long workbookId, Long problemId, ProblemUpdateRequest dto){

        Workbook workbook = validateUserAndWorkbook(workbookId);

        Problem problem = problemRepository.findById(problemId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제입니다.")
        );

        if(!workbook.getId().equals(problem.getWorkbook().getId())){
            throw new ProblemNotInWorkbook("문제집에 존재하지 않는 문제입니다.");
        }

        problem.update(dto.getQuestion(), dto.getSolution(), LocalDateTime.now());

        return ProblemResponse.fromEntity(problem);
    }

    @Transactional
    public void delete(Long workbookId, Long problemId){
        Workbook workbook = validateUserAndWorkbook(workbookId);

        Problem problem = problemRepository.findById(problemId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제입니다.")
        );

        if(!workbook.getId().equals(problem.getWorkbook().getId())){
            throw new ProblemNotInWorkbook("문제집에 존재하지 않는 문제입니다.");
        }

        problemRepository.delete(problem);
    }



    private User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        return user;
    }

}