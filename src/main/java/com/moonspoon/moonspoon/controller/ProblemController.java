package com.moonspoon.moonspoon.controller;

import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.response.ProblemResponse;
import com.moonspoon.moonspoon.dto.response.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.TestResultResponse;
import com.moonspoon.moonspoon.service.ProblemService;
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
    public ResponseEntity<ProblemResponse> createProblem(
            @PathVariable("workbookId") Long workbookId,
            @Valid @RequestBody ProblemCreateRequest dto){
        ProblemResponse response =  problemService.create(workbookId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProblemResponse>> findAllProblem(@PathVariable("workbookId") Long workbookId){
        List<ProblemResponse> responses = problemService.findAll(workbookId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
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

    @PostMapping("/getTestResult")
    public ResponseEntity<List<TestResultResponse>> getTestResultProblem(
            @PathVariable("workbookId") Long workbookId,
            @RequestBody List<TestResultRequest> dto){
        List<TestResultResponse> responses = problemService.getTestResultProblem(workbookId, dto);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
