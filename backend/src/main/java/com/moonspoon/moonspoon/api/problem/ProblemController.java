package com.moonspoon.moonspoon.api.problem;

import com.moonspoon.moonspoon.api.problem.dto.request.ProblemCreateRequest;
import com.moonspoon.moonspoon.api.problem.dto.request.ProblemUpdateRequest;
import com.moonspoon.moonspoon.api.problem.dto.response.ProblemAllResponse;
import com.moonspoon.moonspoon.api.problem.dto.response.ProblemCreateResponse;
import com.moonspoon.moonspoon.api.problem.dto.response.ProblemResponse;
import com.moonspoon.moonspoon.core.problem.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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