package com.moonspoon.moonspoon.sharedWorkbook;

import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookRequest;
import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookGetUserResponse;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookResponse;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookTestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sharedWorkbook")
public class SharedWorkbookController {
    private final SharedWorkbookService sharedWorkbookService;
    //todo url 규칙 Restful하게 변경

    //단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<SharedWorkbookResponse> findSharedWorkbook(@PathVariable("id") Long id){
        SharedWorkbookResponse response = sharedWorkbookService.findSharedWorkbook(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //전체 조회
    @GetMapping("/all")
    public ResponseEntity<Page<SharedWorkbookResponse>> findAllSharedWorkbook(@RequestParam(name="page", defaultValue = "0") int page,
                                                                              @RequestParam(name="size", defaultValue = "12") int size){
        Page<SharedWorkbookResponse> responses = sharedWorkbookService.findAllSharedWorkbook(page, size);
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


    //사용자 검증
    @GetMapping("/{id}/getUser")
    public ResponseEntity<SharedWorkbookGetUserResponse> getUser(@PathVariable("id") Long id){
        SharedWorkbookGetUserResponse response = sharedWorkbookService.getUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
