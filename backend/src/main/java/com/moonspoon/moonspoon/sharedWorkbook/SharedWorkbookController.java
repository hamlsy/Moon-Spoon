package com.moonspoon.moonspoon.sharedWorkbook;

import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookRequest;
import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sharedWorkbook")
public class SharedWorkbookController {
    private final SharedWorkbookService sharedWorkbookService;


    //단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> findSharedWorkbook(@PathVariable("id") Long id){
        return null;
    }

    //전체 조회
    @GetMapping("/all")
    public ResponseEntity<?> findAllSharedWorkbook(){
        return null;
    }

    //공유 문제집 등록
    @PostMapping("/")
    public ResponseEntity<?> createSharedWorkbook(@RequestBody SharedWorkbookRequest dto){
        return null;
    }

    //수정
    @PostMapping("/{id}")
    public ResponseEntity<?> updateSharedWorkbook(@PathVariable("id") Long id , @RequestBody SharedWorkbookUpdateRequest dto){
        return null;
    }

    //삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSharedWorkbook(@PathVariable("id") Long id){
        return null;
    }
}
