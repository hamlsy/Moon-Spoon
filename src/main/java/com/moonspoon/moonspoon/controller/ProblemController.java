package com.moonspoon.moonspoon.controller;

import com.moonspoon.moonspoon.dto.request.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.response.ProblemResponse;
import com.moonspoon.moonspoon.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
        ProblemResponse response =  problemService.create(dto, workbookId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProblemResponse>> findAllProblem(@PathVariable("workbookId") Long workbookId){
        List<ProblemResponse> responses = problemService.findAll(workbookId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
