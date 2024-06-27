package com.moonspoon.moonspoon.controller;

import com.moonspoon.moonspoon.dto.request.WorkbookCreateRequest;
import com.moonspoon.moonspoon.dto.request.WorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.WorkbookResponse;
import com.moonspoon.moonspoon.service.WorkbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workbook")
public class WorkbookController {
    private final WorkbookService workbookService;

    @PostMapping("/create")
    public ResponseEntity<WorkbookResponse> createWorkbook(@RequestBody WorkbookCreateRequest dto){
        WorkbookResponse response = workbookService.createWorkbook(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkbookResponse> findWorkbook(@PathVariable("id") Long id){
        WorkbookResponse response = workbookService.findOneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WorkbookResponse>> findAllWorkbook(){
        List<WorkbookResponse> workbooks = workbookService.findAll();
        return new ResponseEntity<>(workbooks, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<WorkbookResponse> updateWorkbook(@RequestBody WorkbookUpdateRequest dto, @PathVariable("id") Long id){
        WorkbookResponse response = workbookService.updateWorkbook(id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkbook(@PathVariable("id") Long id){
        workbookService.deleteWorkbook(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
