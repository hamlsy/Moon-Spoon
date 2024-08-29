package com.moonspoon.moonspoon.localTest;

import com.moonspoon.moonspoon.dto.request.test.TestIncorrectProblemRequest;
import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultSubmitResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.problem.Problem;
import com.moonspoon.moonspoon.problem.ProblemRepository;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRepository;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.workbook.WorkbookRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalTestService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private Map<String, List<TestResultRequest>> storedLists = new ConcurrentHashMap<>();

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

    public List<TestProblemResponse> getTestIncorrectProblems(TestIncorrectProblemRequest dto){
        if(dto.getIncorrectProblemIds().isEmpty()){
            throw new NotFoundException("틀린 문제가 존재하지 않습니다.");
        }
        List<Problem> problems = problemRepository.findAllById(dto.getIncorrectProblemIds());
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
        //검증 로직
        validateUserAndWorkbook(workbookId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TestResultRequest> dto = storedLists.get(username);
        List<Long> problemIds = dto.stream().map(TestResultRequest::getId).collect(Collectors.toList());

        List<Problem> problems = problemRepository.findByIdList(problemIds);
        Map<Long, Problem> problemMap = problems.stream()
                .collect(Collectors.toMap(Problem::getId, problem -> problem));

        List<TestResultResponse> responses =  dto.stream()
                .map(d -> {
                    Problem problem = problemMap.get(d.getId());
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
    public TestResultSubmitResponse testResultSubmit(Long workbookId, List<TestResultSubmitRequest> dto){
        //검증 로직
        validateUserAndWorkbook(workbookId);
        //계산 및 검증로직
        int size = dto.size();
        int correctCount = 0;
        for(int i = 0; i < dto.size(); i++){
            if(dto.get(i).getResult().equals("correct")){
                correctCount++;
            }
        }
        //내 문제집 테스트 횟수 증가
        User user = getCurrentUser();
        user.addWorkbookTestCount();

        TestResultSubmitResponse response = TestResultSubmitResponse.builder()
                .correctCount(correctCount)
                .incorrectCount(size-correctCount)
                .score(((double)correctCount/size)*100)
                .problems(dto)
                .build();

        // 벌크 연산
        updateCorrectRate(dto);

        return response;
    }

    private void updateCorrectRate(List<TestResultSubmitRequest> dto){
        List<Long> correctProblemIds = new ArrayList<>();
        List<Long> incorrectProblemIds = new ArrayList<>();

        for (TestResultSubmitRequest result : dto) {
            if ("correct".equals(result.getResult())) {
                correctProblemIds.add(result.getId());
            } else {
                incorrectProblemIds.add(result.getId());
            }
        }
        if (!correctProblemIds.isEmpty()) {
            em.createQuery("UPDATE Problem p SET p.correctCount = p.correctCount + 1 WHERE p.id IN :ids")
                    .setParameter("ids", correctProblemIds)
                    .executeUpdate();
        }

        if (!incorrectProblemIds.isEmpty()) {
            em.createQuery("UPDATE Problem p SET p.incorrectCount = p.incorrectCount + 1 WHERE p.id IN :ids")
                    .setParameter("ids", incorrectProblemIds)
                    .executeUpdate();
        }

    }

    public void storeInputData(Long workbookId ,List<TestResultRequest> listDto){
        //검증 로직
        validateUserAndWorkbook(workbookId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        this.storedLists.put(username, listDto);
    }


    private User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        return user;
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
}
