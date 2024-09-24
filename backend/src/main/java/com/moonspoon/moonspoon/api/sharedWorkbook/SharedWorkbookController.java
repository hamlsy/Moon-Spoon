package com.moonspoon.moonspoon.api.sharedWorkbook;

import com.moonspoon.moonspoon.api.sharedWorkbook.dto.request.SharedWorkbookRequest;
import com.moonspoon.moonspoon.api.sharedWorkbook.dto.request.SharedWorkbookUpdateRequest;
import com.moonspoon.moonspoon.api.sharedWorkbook.dto.response.SharedWorkbookListResponse;
import com.moonspoon.moonspoon.api.sharedWorkbook.dto.response.SharedWorkbookResponse;
import com.moonspoon.moonspoon.core.sharedWorkbook.SharedWorkbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sharedWorkbook")
public class SharedWorkbookController {
    private final SharedWorkbookService sharedWorkbookService;
    //todo url 규칙 Restful하게 변경

    //단일 조회 + 댓글 조회 + 사용자 검증
    @GetMapping("/{id}")
    public ResponseEntity<SharedWorkbookResponse> findSharedWorkbook(@PathVariable("id") Long id){
        SharedWorkbookResponse response = sharedWorkbookService.findSharedWorkbook(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //전체 조회
    @GetMapping("/all")
    public ResponseEntity<Page<SharedWorkbookListResponse>> findAllSharedWorkbook(
            @RequestParam(name="keyword", defaultValue = "") String keyword,
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "12") int size){
        Page<SharedWorkbookListResponse> responses = sharedWorkbookService.findAllSharedWorkbook(keyword, page, size);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    //공유 문제집 등록
    @PostMapping("/create")
    public ResponseEntity<SharedWorkbookResponse> createSharedWorkbook(@RequestBody SharedWorkbookRequest dto){
        SharedWorkbookResponse response = sharedWorkbookService.sharedWorkbook(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //수정
    @PostMapping("/{id}")
    public ResponseEntity<SharedWorkbookResponse> updateSharedWorkbook(@PathVariable("id") Long id , @RequestBody SharedWorkbookUpdateRequest dto){
        SharedWorkbookResponse response = sharedWorkbookService.updateSharedWorkbook(id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSharedWorkbook(@PathVariable("id") Long id){
        sharedWorkbookService.deleteSharedWorkbook(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}
