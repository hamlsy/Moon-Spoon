package com.moonspoon.moonspoon.problem;

import com.moonspoon.moonspoon.dto.request.problem.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.request.problem.ProblemUpdateRequest;
import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.response.problem.ProblemAllResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemCreateResponse;
import com.moonspoon.moonspoon.dto.response.problem.ProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultSubmitResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ProblemAllResponse> findAllProblem(
            @PathVariable("workbookId") Long workbookId,
            @RequestParam(name="keyword", defaultValue = "") String keyword,
            @RequestParam(name="order", defaultValue = "newest") String order,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "16") int size
            ){
        ProblemAllResponse response = problemService.findAll(workbookId, keyword, order, page, size);
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


}