package com.moonspoon.moonspoon.problem;

import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.response.problem.ProblemCreateResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemFindAllResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultSubmitResponse;
import com.moonspoon.moonspoon.problem.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workbook/{workbookId}/problem")
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping("/create")
    public ResponseEntity<ProblemCreateResponse> createProblem(
            @PathVariable("workbookId") Long workbookId,
            @Valid @RequestBody ProblemCreateRequest dto){
        ProblemCreateResponse response =  problemService.create(workbookId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ProblemFindAllResponse> findAllProblem(@PathVariable("workbookId") Long workbookId){
        ProblemFindAllResponse response = problemService.findAllWithWorkbookTitle(workbookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ProblemResponse> updateProblem(
            @PathVariable("workbookId") Long workbookId, @PathVariable("id") Long id
            , @RequestBody ProblemUpdateRequest dto) {
        ProblemResponse response = problemService.update(workbookId, id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProblem(
            @PathVariable("workbookId") Long workbookId, @PathVariable("id") Long id){
        problemService.delete(workbookId, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/getTest")
    public ResponseEntity<List<TestProblemResponse>> getTestProblem(
            @PathVariable("workbookId") Long workbookId, @RequestBody TestRequest dto){
        List<TestProblemResponse> responses = problemService.getTestProblems(workbookId, dto);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/getTestResult")
    public ResponseEntity<List<TestResultResponse>> getTestResultProblem(
            @PathVariable("workbookId") Long workbookId){
        List<TestResultResponse> responses = problemService.getTestResultProblem(workbookId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/submitTestResult")
    public ResponseEntity<TestResultSubmitResponse> submitTestResult(
            @PathVariable("workbookId") Long workbookId, @RequestBody List<TestResultSubmitRequest> dto){
        TestResultSubmitResponse response = problemService.testResultSubmit(workbookId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/storeTest")
    public ResponseEntity<?> storeTest(
            @PathVariable("workbookId") Long workbookId, @RequestBody List<TestResultRequest> listDto){
        problemService.storeInputData(workbookId, listDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}