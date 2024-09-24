package com.moonspoon.moonspoon.core.test;

import com.moonspoon.moonspoon.api.test.dto.request.TestResultRequest;
import com.moonspoon.moonspoon.api.test.dto.request.TestResultSubmitRequest;
import com.moonspoon.moonspoon.api.test.dto.response.*;
import com.moonspoon.moonspoon.common.exception.custom.NotFoundException;
import com.moonspoon.moonspoon.common.exception.custom.NotUserException;
import com.moonspoon.moonspoon.core.problem.Problem;
import com.moonspoon.moonspoon.core.problem.ProblemRepository;
import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbookRepository;
import com.moonspoon.moonspoon.core.testAnswer.TestAnswer;
import com.moonspoon.moonspoon.core.user.User;
import com.moonspoon.moonspoon.core.user.UserRepository;

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
    private final JdbcTemplate jdbcTemplate;

    private static final String SHARED_WORKBOOK_NOT_FOUND_MESSAGE = "존재하지 않는 문제집입니다.";
    private static final String TEST_NOT_FOUND_MESSAGE = "존재하지 않는 테스트입니다.";
    private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다.";

    private SharedWorkbook validateUserAndSharedWorkbook(Long sharedWorkbookId) {
        String username = getCurrentUsername();
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException(UNAUTHORIZED_MESSAGE);
        }
        //문제집 예외
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findById(sharedWorkbookId).orElseThrow(
                () -> new NotFoundException(SHARED_WORKBOOK_NOT_FOUND_MESSAGE)
        );
        return sharedWorkbook;
    }

    private Test createSharedTest(Long id){
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(UNAUTHORIZED_MESSAGE)
        );
        SharedWorkbook sharedWorkbook = validateUserAndSharedWorkbook(id);
        Test test = new Test();
        test.setTestDate(LocalDateTime.now());
        test.setUser(user);
        test.setName(user.getName());
        test.setSharedWorkbook(sharedWorkbook);

        return testRepository.save(test);
    }

    // CreateTest 하면서 Get Test Problem
    //todo refactor
    @Transactional
    public TestSharedResponse getSharedTest(Long sharedWorkbookId){
        Test test = createSharedTest(sharedWorkbookId);
        SharedWorkbook sharedWorkbook = test.getSharedWorkbook();
        List<Problem> problems = problemRepository.findAllBySharedWorkbookId(sharedWorkbookId);
        if(sharedWorkbook.isRandom()){
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
                () -> new NotFoundException(TEST_NOT_FOUND_MESSAGE)
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
                        () -> new NotFoundException(TEST_NOT_FOUND_MESSAGE)
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
                        () -> new NotFoundException(TEST_NOT_FOUND_MESSAGE)
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

    public List<TestSharedPracticeResponse> getPracticeProblems(Long sharedWorkbookId){
        List<Problem> problems = problemRepository.findAllBySharedWorkbookId(sharedWorkbookId);
        List<TestSharedPracticeResponse> responses = problems.stream().map(
                TestSharedPracticeResponse::fromEntity
        ).collect(Collectors.toList());
        return responses;
    }

}
