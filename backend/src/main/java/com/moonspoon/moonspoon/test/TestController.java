package com.moonspoon.moonspoon.test;

import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultSubmitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final TestService testService;


    @PostMapping("/getTest")
    public ResponseEntity<List<TestProblemResponse>> getTestProblem(@RequestBody TestRequest dto){
        List<TestProblemResponse> responses = testService.getTestProblems(dto);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/getTestResult")
    public ResponseEntity<List<TestResultResponse>> getTestResultProblem(
            @PathVariable("workbookId") Long workbookId){
        List<TestResultResponse> responses = testService.getTestResultProblem(workbookId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/submitTestResult")
    public ResponseEntity<TestResultSubmitResponse> submitTestResult(
            @PathVariable("workbookId") Long workbookId, @RequestBody List<TestResultSubmitRequest> dto){
        TestResultSubmitResponse response = testService.testResultSubmit(workbookId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/storeTest")
    public ResponseEntity<?> storeTest(
            @PathVariable("workbookId") Long workbookId, @RequestBody List<TestResultRequest> listDto){
        testService.storeInputData(workbookId, listDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
