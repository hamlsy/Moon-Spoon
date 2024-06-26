package com.moonspoon.moonspoon.controller;

import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.service.WorkbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/workbook")
public class WorkbookController {
    private final WorkbookService workbookService;

    @PostMapping("/create")
    public ResponseEntity<Workbook> createWorkbook(@RequestBody Workbook workbook){
        Workbook createdWorkbook = workbookService.createWorkbook(workbook);
        return new ResponseEntity<>(createdWorkbook, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workbook> findWorkbook(@PathVariable("id") Long id){
        Workbook workbook = workbookService.findOneById(id);
        return new ResponseEntity<>(workbook, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Workbook>> findAllWorkbook(){
        List<Workbook> workbooks = workbookService.findAll();
        return new ResponseEntity<>(workbooks, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Workbook> updateWorkbook(@RequestBody Workbook workbook,@PathVariable("id") Long id){
        Workbook updatedWorkbook = workbookService.updateWorkbook(id, workbook);
        return new ResponseEntity<>(updatedWorkbook, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkbook(@PathVariable("id") Long id){
        workbookService.deleteWorkbook(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
