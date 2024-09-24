package com.moonspoon.moonspoon.api.workbook;

import com.moonspoon.moonspoon.api.workbook.dto.request.WorkbookCreateRequest;
import com.moonspoon.moonspoon.api.workbook.dto.request.WorkbookUpdateRequest;
import com.moonspoon.moonspoon.api.workbook.dto.response.WorkbookResponse;
import com.moonspoon.moonspoon.core.workbook.WorkbookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workbook")
public class WorkbookController {
    private final WorkbookService workbookService;

    @PostMapping("/create")
    public ResponseEntity<WorkbookResponse> createWorkbook(@Valid @RequestBody WorkbookCreateRequest dto) {
        WorkbookResponse response = workbookService.createWorkbook(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkbookResponse> findWorkbook(@PathVariable("id") Long id){
        WorkbookResponse response = workbookService.findOneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<WorkbookResponse>> findAllWorkbook(
            @RequestParam(name="keyword", defaultValue = "") String keyword,
            @RequestParam(name="order", defaultValue = "newest") String order,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "5") int size){
        Page<WorkbookResponse> workbooks = workbookService.findAll(keyword, order, page, size);
        return new ResponseEntity<>(workbooks, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<WorkbookResponse> updateWorkbook(@Valid @RequestBody WorkbookUpdateRequest dto, @PathVariable("id") Long id){
        WorkbookResponse response = workbookService.updateWorkbook(id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkbook(@PathVariable("id") Long id){
        workbookService.deleteWorkbook(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
