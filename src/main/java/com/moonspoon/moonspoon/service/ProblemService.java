package com.moonspoon.moonspoon.service;

import com.moonspoon.moonspoon.domain.Problem;
import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.request.test.TestInputDTO;
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
import com.moonspoon.moonspoon.repository.ProblemRepository;
import com.moonspoon.moonspoon.repository.WorkbookRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;
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

    //Test logic
    public List<TestProblemResponse> getTestProblems(Long workbookId , TestRequest dto){

        Workbook workbook = validateUserAndWorkbook(workbookId);

        List<Problem> problems = workbook.getProblems();

        int selectCount = Math.min(dto.getProblemCount(), problems.size());

        if(dto.isRandom() && !dto.getSortOrder().equals("none")){
            //순서 정렬
            problems = setOrderProblemList(dto.getSortOrder(), problems).subList(0, selectCount);
            Collections.shuffle(problems);
        }else if(dto.isRandom() && dto.getSortOrder().equals("none")){
            Collections.shuffle(problems);
            problems = problems.subList(0, selectCount);
        }else if(!dto.isRandom() && !dto.getSortOrder().equals("none")){
            //순서 정렬
            problems = setOrderProblemList(dto.getSortOrder(), problems).subList(0, selectCount);
        }else{
            //순서 정렬
            problems = setOrderProblemList("asc", problems).subList(0, selectCount);
        }

        return problems.stream()
                .map(p -> TestProblemResponse.fromEntity(p))
                .collect(Collectors.toList());
    }

    private List<Problem> setOrderProblemList(String order, List<Problem> problems){
        switch(order){
            case "asc":
                return sortByCreateDateAsc(problems);
            case "desc":
                return sortByCreateDateDesc(problems);
            case "correctRateAsc":
                return sortByCorrectRateAsc(problems);
            case "correctRateDesc":
                return sortByCorrectRateDesc(problems);
            default:
                return problems;
        }
    }

    private List<Problem> sortByCorrectRateAsc(List<Problem> problems){
        return problems.stream()
                .sorted(Comparator.comparingDouble(Problem::getCorrectRate))
                .collect(Collectors.toList());
    }

    private List<Problem> sortByCorrectRateDesc(List<Problem> problems){
        return problems.stream()
                .sorted(Comparator.comparingDouble(Problem::getCorrectRate).reversed())
                .collect(Collectors.toList());
    }

    private List<Problem> sortByCreateDateAsc(List<Problem> problems){
        return problems.stream()
                .sorted(Comparator.comparing(Problem::getCreateDate))
                .collect(Collectors.toList());
    }

    private List<Problem> sortByCreateDateDesc(List<Problem> problems){
        return problems.stream()
                .sorted(Comparator.comparing(Problem::getCreateDate).reversed())
                .collect(Collectors.toList());
    }

    public List<TestResultResponse> getTestResultProblem(Long workbookId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TestResultRequest> dto = storedLists.get(username);
        //검증 로직
        validateUserAndWorkbook(workbookId);
        List<TestResultResponse> responses =  dto.stream()
                .map(d -> {
                    Problem problem = problemRepository.findById(d.getId()).orElseThrow(
                            () -> new NotFoundException("존재하지 않는 문제입니다.")
                    );
                    TestResultResponse res = TestResultResponse.fromEntity(problem);
                    res.setInput(d.getInput());
                    res.setResult(compareStrings(d.getInput(), problem.getSolution()));
                    return res;
                        })
                .collect(Collectors.toList());

        return responses;

    }

    private String compareStrings(String input, String sol){
        String inputData = input.replaceAll("\\s", "").toLowerCase();
        String solution = sol.replaceAll("\\s", "").toLowerCase();
        if(inputData.equals(solution)){
            return "correct";
        }else{
            return "incorrect";
        }
    }

    @Transactional
    public List<TestResultSubmitResponse> testResultSubmit(Long workbookId, List<TestResultSubmitRequest> dto){
        //검증 로직
        validateUserAndWorkbook(workbookId);
        List<TestResultSubmitResponse> responses = dto.stream()
                .map(d -> {
                    Problem problem = problemRepository.findById(d.getId()).orElseThrow(
                            () -> new NotFoundException("존재하지 않는 문제입니다.")
                    );
                    calculateCorrectRate(problem, d.getResult());
                    TestResultSubmitResponse res =  TestResultSubmitResponse.fromEntity(problem);
                    res.setResult(d.getResult());
                    return res;
                })
                .collect(Collectors.toList());
        return responses;
    }

    private void calculateCorrectRate(Problem problem, String result){
        if(result.equals("correct")){
            problem.addCorrectCount();
        }else{
            problem.addIncorrectCount();
        }
    }

    public void storeInputData(Long workbookId ,List<TestResultRequest> listDto){
        //검증 로직
        validateUserAndWorkbook(workbookId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        this.storedLists.put(username, listDto);
    }

}
