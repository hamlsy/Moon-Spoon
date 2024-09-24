package com.moonspoon.moonspoon.core.test;

import com.moonspoon.moonspoon.api.test.dto.request.TestResultRequest;
import com.moonspoon.moonspoon.api.test.dto.request.TestResultSubmitRequest;
import com.moonspoon.moonspoon.api.test.dto.response.*;
import com.moonspoon.moonspoon.common.exception.custom.NotFoundException;
import com.moonspoon.moonspoon.common.utils.Utils;
import com.moonspoon.moonspoon.common.validation.UserValidator;
import com.moonspoon.moonspoon.core.problem.Problem;
import com.moonspoon.moonspoon.core.problem.ProblemRepository;
import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbookRepository;
import com.moonspoon.moonspoon.core.testAnswer.TestAnswer;
import com.moonspoon.moonspoon.core.user.User;
import com.moonspoon.moonspoon.core.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final UserValidator validator;

    private static final String CORRECT_RESULT_STRING = "correct";
    private static final String INCORRECT_RESULT_STRING = "incorrect";
    private static final String SHARED_WORKBOOK_NOT_FOUND_MESSAGE = "존재하지 않는 문제집입니다.";
    private static final String TEST_NOT_FOUND_MESSAGE = "존재하지 않는 테스트입니다.";
    private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다.";

    private SharedWorkbook validateUserAndSharedWorkbook(Long sharedWorkbookId) {
        validator.validateCurrentUser();
        //문제집 예외
        return sharedWorkbookRepository.findById(sharedWorkbookId).orElseThrow(
                () -> new NotFoundException(SHARED_WORKBOOK_NOT_FOUND_MESSAGE));
    }

    private Test createSharedTest(Long id){
        String username = Utils.getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(UNAUTHORIZED_MESSAGE)
        );
        SharedWorkbook sharedWorkbook = validateUserAndSharedWorkbook(id);
        Test test = Test.builder()
                .user(user)
                .sharedWorkbook(sharedWorkbook)
                .name(user.getName())
                .testDate(LocalDateTime.now())
                .build();

        return testRepository.save(test);
    }

    // CreateTest 하면서 Get Test Problem
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

        return TestSharedResponse.builder()
                .testSharedProblems(testSharedProblems)
                .testId(test.getId())
                .build();
    }

    //답변 저장
    @Transactional
    public void submitSharedTest(Long id, List<TestResultRequest> listDto){
        Test test = findTestById(id);
        List<Long> problemIds = listDto.stream().map(TestResultRequest::getId).collect(Collectors.toList());
        List<Problem> problems = problemRepository.findAllById(problemIds);
        Map<Long, Problem> problemMap = problems.stream()
                .collect(Collectors.toMap(Problem::getId, problem -> problem));

        bulkInsert(createTestAnswers(problemMap, test, listDto));
    }

    private  List<TestAnswer> createTestAnswers(Map<Long, Problem> problemMap, Test test, List<TestResultRequest> listDto){
        String currentUser = Utils.getCurrentUsername();
        return listDto.stream()
                .map(d -> TestAnswer.builder()
                        .userAnswer(d.getInput())
                        .name(currentUser)
                        .problem(problemMap.get(d.getId()))
                        .test(test)
                        .build()).collect(Collectors.toList());
    }

    private Test findTestById(Long id){
        return testRepository.findById(id).orElseThrow(
                () -> new NotFoundException(TEST_NOT_FOUND_MESSAGE));
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
        Test test = testRepository.findById(testId)
                .orElseThrow(
                        () -> new NotFoundException(TEST_NOT_FOUND_MESSAGE)
                );
        double correctCount = 0;
        double incorrectCount = 0;
        for(TestResultSubmitRequest dto : listDto){
            if(dto.getResult().equals(CORRECT_RESULT_STRING)){
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
        return inputData.equals(solution) ? CORRECT_RESULT_STRING : INCORRECT_RESULT_STRING;
    }

    public List<TestSharedPracticeResponse> getPracticeProblems(Long sharedWorkbookId){
        List<Problem> problems = problemRepository.findAllBySharedWorkbookId(sharedWorkbookId);

        return problems.stream().map(TestSharedPracticeResponse::fromEntity)
                .collect(Collectors.toList());
    }

}
