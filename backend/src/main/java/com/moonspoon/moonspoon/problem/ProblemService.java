package com.moonspoon.moonspoon.problem;

import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbookRepository;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.response.problem.ProblemCreateResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemFindAllResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultSubmitResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.exception.ProblemNotInWorkbook;
import com.moonspoon.moonspoon.workbook.WorkbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;


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

    public List<ProblemResponse> findAll(Workbook workbook){
        List<Problem> problems = problemRepository.findAllByWorkbookId(workbook.getId());
        return problems.stream()
                .map(p -> ProblemResponse.fromEntity(p))
                .collect(Collectors.toList());
    }

    public ProblemFindAllResponse findAllWithWorkbookTitle(Long workbookId){
        Workbook workbook = validateUserAndWorkbook(workbookId);
        ProblemFindAllResponse response = new ProblemFindAllResponse();
        response.setProblems(findAll(workbook));
        response.setWorkbookTitle(workbook.getTitle());
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



}
