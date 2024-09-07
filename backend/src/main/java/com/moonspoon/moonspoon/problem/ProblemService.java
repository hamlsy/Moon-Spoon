package com.moonspoon.moonspoon.problem;

import com.moonspoon.moonspoon.dto.response.problem.ProblemAllResponse;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRepository;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.response.problem.ProblemCreateResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemResponse;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;

    private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다.";
    private static final String PROBLEM_NOT_FOUND_MESSAGE = "존재하지 않는 문제입니다.";
    private static final String PROBLEM_NOT_IN_WORKBOOK_MESSAGE = "문제집에 존재하지 않는 문제입니다.";

    private static final String CREATE_DATE_SORT_FIELD = "createDate";
    private static final String CORRECT_RATE_SORT_FIELD = "correctRate";


    @Transactional
    public ProblemCreateResponse create(Long workbookId, ProblemCreateRequest dto){
        Workbook workbook = validateUserAndWorkbook(workbookId);

        Problem problem = ProblemCreateRequest.toEntity(dto);
        problem.setWorkbook(workbook);

        problemRepository.save(problem);
        return ProblemCreateResponse.fromEntity(problem);
    }
    private Workbook validateUserAndWorkbook(Long workbookId) {
        String username = getCurrentUsername();
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException(UNAUTHORIZED_MESSAGE);
        }
        //문제집 예외
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        //사용자 예외
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException(UNAUTHORIZED_MESSAGE);
        }
        return workbook;
    }

    //전체 조회
    public ProblemAllResponse findAll(Long workbookId, String keyword, String order, int page, int size){
        Workbook workbook = validateUserAndWorkbook(workbookId);

        Pageable pageable = createPageable(page, size, order);
        Page<Problem> problems = problemRepository.findAllByWorkbookIdAndKeyword(workbookId, keyword.trim(), pageable);
        Page<ProblemResponse> problemResponses = problems.map(ProblemResponse::fromEntity);

        ProblemAllResponse response = new ProblemAllResponse();
        response.setWorkbookTitle(workbook.getTitle());
        response.setProblems(problemResponses);
        return response;
    }

    private Pageable createPageable(int page, int size, String order){
        Sort sort = Sort.by(CREATE_DATE_SORT_FIELD).descending();
        switch (order){
            case "oldest":
                sort = Sort.by(CREATE_DATE_SORT_FIELD).ascending();
                break;
            case "correctRateAsc":
                sort = Sort.by(CORRECT_RATE_SORT_FIELD).ascending();
                break;
            case "correctRateDesc":
                sort = Sort.by(CORRECT_RATE_SORT_FIELD).descending();
                break;
        }
        return PageRequest.of(page, size, sort);
    }

    @Transactional
    public ProblemResponse update(Long workbookId, Long problemId, ProblemUpdateRequest dto){
        Problem problem = validatedProblem(problemId);
        validatedProblemInWorkbook(workbookId, problemId);

        problem.update(dto.getQuestion(), dto.getSolution(), LocalDateTime.now());

        return ProblemResponse.fromEntity(problem);
    }

    @Transactional
    public void delete(Long workbookId, Long problemId){
        Problem problem = validatedProblem(problemId);
        validatedProblemInWorkbook(workbookId, problemId);
        problemRepository.delete(problem);
    }

    private void validatedProblemInWorkbook(Long workbookId, Long problemId){
        if(problemRepository.countByIdAndWorkbookId(problemId, workbookId) <= 0) {
            throw new ProblemNotInWorkbook(PROBLEM_NOT_IN_WORKBOOK_MESSAGE);
        }
    }

    private Problem validatedProblem(Long problemId){
        return problemRepository.findById(problemId).orElseThrow(
                () -> new NotFoundException(PROBLEM_NOT_FOUND_MESSAGE));
    }

    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }



}