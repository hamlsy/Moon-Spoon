package com.moonspoon.moonspoon.localTest;

import com.moonspoon.moonspoon.dto.request.test.TestIncorrectProblemRequest;
import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.response.test.localTest.TestPracticeResponse;
import com.moonspoon.moonspoon.dto.response.test.localTest.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.localTest.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.localTest.TestResultSubmitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workbook/{workbookId}/localTest")
public class LocalTestController {

    private final LocalTestService localTestService;

    @PostMapping("/getTest")
    public ResponseEntity<List<TestProblemResponse>> getTestProblem(
            @PathVariable("workbookId") Long workbookId, @RequestBody TestRequest dto){
        List<TestProblemResponse> responses = localTestService.getTestProblems(workbookId, dto);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/getTestResult")
    public ResponseEntity<List<TestResultResponse>> getTestResultProblem(
            @PathVariable("workbookId") Long workbookId){
        List<TestResultResponse> responses = localTestService.getTestResultProblem(workbookId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/submitTestResult")
    public ResponseEntity<TestResultSubmitResponse> submitTestResult(
            @PathVariable("workbookId") Long workbookId, @RequestBody List<TestResultSubmitRequest> dto){
        TestResultSubmitResponse response = localTestService.testResultSubmit(workbookId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/storeTest")
    public ResponseEntity<?> storeTest(
            @PathVariable("workbookId") Long workbookId, @RequestBody List<TestResultRequest> listDto){
        localTestService.storeInputData(workbookId, listDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/getIncorrectTest")
    public ResponseEntity<List<TestProblemResponse>> getIncorrectTestProblem(@PathVariable("workbookId") Long workbookId, @RequestBody TestIncorrectProblemRequest dto){
        List<TestProblemResponse> responses = localTestService.getTestIncorrectProblems(dto);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/getLocalPractice")
    public ResponseEntity<List<TestPracticeResponse>> getLocalPractice(@PathVariable("workbookId") Long workbookId){
        List<TestPracticeResponse> responses = localTestService.getPracticeProblems(workbookId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
