package com.moonspoon.moonspoon.api.test;


import com.moonspoon.moonspoon.api.test.dto.request.TestResultRequest;
import com.moonspoon.moonspoon.api.test.dto.request.TestResultSubmitRequest;

import com.moonspoon.moonspoon.api.test.dto.response.TestSharedPracticeResponse;
import com.moonspoon.moonspoon.api.test.dto.response.TestSharedResponse;
import com.moonspoon.moonspoon.api.test.dto.response.TestSharedResultResponse;
import com.moonspoon.moonspoon.api.test.dto.response.TestSharedResultSubmitResponse;
import com.moonspoon.moonspoon.core.test.TestService;
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
    @GetMapping("/getSharedTest")
    public ResponseEntity<TestSharedResponse> getSharedTestProblem(@PathVariable("id") Long sharedWorkbookId){
        TestSharedResponse response = testService.getSharedTest(sharedWorkbookId);
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
