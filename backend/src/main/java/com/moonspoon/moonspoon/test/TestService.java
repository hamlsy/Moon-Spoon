package com.moonspoon.moonspoon.test;

import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.request.test.TestSharedWorkbookRequest;
import com.moonspoon.moonspoon.dto.response.test.*;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.problem.Problem;
import com.moonspoon.moonspoon.problem.ProblemRepository;
import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbookRepository;
import com.moonspoon.moonspoon.testAnswer.TestAnswer;
import com.moonspoon.moonspoon.testAnswer.TestAnswerRepository;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {
    private final ProblemRepository problemRepository;
    private final SharedWorkbookRepository sharedWorkbookRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final TestAnswerRepository testAnswerRepository;
    private final JdbcTemplate jdbcTemplate;

    private SharedWorkbook validateUserAndSharedWorkbook(Long sharedWorkbookId) {
        String username = getCurrentUsername();
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException("권한이 없습니다.");
        }
        //문제집 예외
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findById(sharedWorkbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        return sharedWorkbook;
    }

    private Test createSharedTest(Long id){
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username);
        SharedWorkbook sharedWorkbook = validateUserAndSharedWorkbook(id);
        Test test = new Test();
        test.setTestDate(LocalDateTime.now());
        test.setUser(user);
        test.setName(user.getName());
        test.setSharedWorkbook(sharedWorkbook);
        Test saveTest = testRepository.save(test);

        return saveTest;
    }

    // CreateTest 하면서 Get Test Problem
    //todo refactor
    @Transactional
    public TestSharedResponse getSharedTest(Long sharedWorkbookId , TestSharedWorkbookRequest dto){
        Test test = createSharedTest(sharedWorkbookId);
        Long workbookId = test.getSharedWorkbook().getWorkbook().getId();
        List<Problem> problems = problemRepository.findAllByWorkbookId(workbookId);
        if(dto.isRandom()){
            Collections.shuffle(problems);
        }
        List<TestSharedProblemResponse> testSharedProblems = problems.stream()
                .map(p -> TestSharedProblemResponse.fromEntity(p))
                .collect(Collectors.toList());

        TestSharedResponse response = TestSharedResponse.builder()
                .testSharedProblems(testSharedProblems)
                .testId(test.getId())
                .build();
        return response;
    }

    //답변 저장
    @Transactional
    public void submitSharedTest(Long id, List<TestResultRequest> listDto){
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 테스트입니다.")
        );

        List<Long> problemIds = listDto.stream().map(TestResultRequest::getId).collect(Collectors.toList());
        List<Problem> problems = problemRepository.findAllById(problemIds);
        Map<Long, Problem> problemMap = problems.stream()
                .collect(Collectors.toMap(Problem::getId, problem -> problem));
        String currentUser = getCurrentUsername();
        List<TestAnswer> answers =  listDto.stream()
                .map(d -> {
                    TestAnswer testAnswer = TestAnswer.builder()
                            .userAnswer(d.getInput())
                            .name(currentUser)
                            .build();
                    Problem problem = problemMap.get(d.getId());
                    testAnswer.setTest(test);
                    testAnswer.setProblem(problem);
                    return testAnswer;
                }).collect(Collectors.toList());
        bulkInsert(answers);
    }

    private void bulkInsert(List<TestAnswer> answers){
        String sql = "INSERT INTO test_answer (name, user_answer, test_id, problem_id) VALUE (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(
                sql, answers, answers.size(),
                (PreparedStatement ps, TestAnswer testAnswer) -> {
                    ps.setString(1, testAnswer.getName());
                    ps.setString(2, testAnswer.getUserAnswer());
                    ps.setLong(3, testAnswer.getTest().getId());
                    ps.setLong(4, testAnswer.getProblem().getId());
                });

    }

    //정답 비교 및 자동 채점 결과 반환
    public List<TestSharedResultResponse> getSharedTestResult(Long id){
        Test test = testRepository.findByIdWithTestAnswersAndProblem(id)
                .orElseThrow(
                        () -> new NotFoundException("존재하지 않는 테스트입니다.")
                );

        List<TestAnswer> testAnswers = test.getTestAnswers();
        List<TestSharedResultResponse> responses =  testAnswers.stream()
                .map(t -> {
                    TestSharedResultResponse response = TestSharedResultResponse.fromEntity(t.getProblem());
                    response.setInput(t.getUserAnswer());
                    response.setResult(compareStrings(
                            t.getUserAnswer(), t.getProblem().getSolution()
                    ));
                    return response;
                }).collect(Collectors.toList());
        return responses;
    }

    //자동채점 결과 제출 및 score 기록
    @Transactional
    public TestSharedResultSubmitResponse submitSharedTestResult(Long testId, List<TestResultSubmitRequest> listDto){
        Test test = testRepository.findByIdWithTestAnswersAndProblem(testId)
                .orElseThrow(
                        () -> new NotFoundException("존재하지 않는 테스트입니다.")
                );
        double correctCount = 0;
        double incorrectCount = 0;
        for(TestResultSubmitRequest dto : listDto){
            if(dto.getResult().equals("correct")){
                correctCount += 1;
            }else{
                incorrectCount += 1;
            }
        }
        double score = ((correctCount)/(correctCount+incorrectCount)) * 100;
        //update
        test.setScore(score);

        TestSharedResultSubmitResponse response =
                TestSharedResultSubmitResponse.builder()
                        .id(testId)
                        .correctCount((int)correctCount)
                        .incorrectCount((int)incorrectCount)
                        .score(score)
                        .build();
        return response;
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


    private String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
