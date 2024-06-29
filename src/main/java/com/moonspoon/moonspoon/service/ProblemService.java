package com.moonspoon.moonspoon.service;

import com.moonspoon.moonspoon.domain.Problem;
import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.response.ProblemResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.exception.ProblemNotInWorkbook;
import com.moonspoon.moonspoon.repository.ProblemRepository;
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
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProblemResponse create(Long workbookId, ProblemCreateRequest dto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);

        //문제집 예외
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        //사용자 예외
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }

        Problem problem = ProblemCreateRequest.toEntitu(dto);
        User user = userRepository.findByUsername(username);

        problem.setWorkbook(workbook);
        problem.setCreateDate(LocalDateTime.now());

        problemRepository.save(problem);
        return ProblemResponse.fromEntity(problem);
    }
    private void validateUser(String username) {
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException("권한이 없습니다.");
        }

    }

    public List<ProblemResponse> findAll(Long workbookId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }

        List<Problem> problems = workbook.getProblems();

        return problems.stream()
                .map(p -> ProblemResponse.fromEntity(p))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProblemResponse update(Long workbookId, Long problemId, ProblemUpdateRequest dto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }

        Problem problem = problemRepository.findById(problemId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제입니다.")
        );

        if(!workbook.getId().equals(problem.getWorkbook().getId())){
            throw new ProblemNotInWorkbook("문제집에 존재하지 않는 문제입니다.");
        }

        problemRepository.delete(problem);
    }
}
