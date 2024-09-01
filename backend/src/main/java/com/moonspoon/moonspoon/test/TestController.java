package com.moonspoon.moonspoon.test;


import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.request.test.TestSharedWorkbookRequest;

import com.moonspoon.moonspoon.dto.response.test.localTest.TestPracticeResponse;
import com.moonspoon.moonspoon.dto.response.test.sharedTest.TestSharedPracticeResponse;
import com.moonspoon.moonspoon.dto.response.test.sharedTest.TestSharedResponse;
import com.moonspoon.moonspoon.dto.response.test.sharedTest.TestSharedResultResponse;
import com.moonspoon.moonspoon.dto.response.test.sharedTest.TestSharedResultSubmitResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test/{id}")
public class TestController {
    private final TestService testService;

    //테스트 시작 + 조회
    @PostMapping("/getSharedTest")
    public ResponseEntity<TestSharedResponse> getSharedTestProblem(@PathVariable("id") Long sharedWorkbookId, @RequestBody TestSharedWorkbookRequest dto){
        TestSharedResponse response = testService.getSharedTest(sharedWorkbookId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //테스트 제출, 입력 답 저장
    @PostMapping("/submitSharedTest")
    public ResponseEntity<?> submitSharedTestAnswer(@PathVariable("id") Long testId, @RequestBody List<TestResultRequest> listDto){
        testService.submitSharedTest(testId, listDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    //테스트 결과 및 답안 조회
    @GetMapping("/getSharedTestResult")
    public ResponseEntity<List<TestSharedResultResponse>> getSharedTestResult(
            @PathVariable("id") Long testId){
        List<TestSharedResultResponse> responses = testService.getSharedTestResult(testId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    //테스트 결과 제출 및 테스트 score 기록
    @PostMapping("/submitSharedTestResult")
    public ResponseEntity<TestSharedResultSubmitResponse> submitSharedTestResult(
            @PathVariable("id") Long testId, @RequestBody List<TestResultSubmitRequest> listDto){
        TestSharedResultSubmitResponse response = testService.submitSharedTestResult(testId, listDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //연습 모드
    @GetMapping("/getPractice")
    public ResponseEntity<List<TestSharedPracticeResponse>> getPractice(@PathVariable("id") Long sharedWorkbookId){
        List<TestSharedPracticeResponse> responses = testService.getPracticeProblems(sharedWorkbookId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}
