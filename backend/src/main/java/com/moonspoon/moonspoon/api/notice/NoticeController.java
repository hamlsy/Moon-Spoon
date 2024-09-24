package com.moonspoon.moonspoon.api.notice;

import com.moonspoon.moonspoon.api.notice.dto.request.NoticeCreateRequest;
import com.moonspoon.moonspoon.api.notice.dto.request.NoticeUpdateRequest;
import com.moonspoon.moonspoon.api.notice.dto.response.NoticeListResponse;
import com.moonspoon.moonspoon.api.notice.dto.response.NoticeResponse;
import com.moonspoon.moonspoon.core.notice.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/create")
    public ResponseEntity<NoticeResponse> createNotice(@Valid @RequestBody NoticeCreateRequest request){
        NoticeResponse response = noticeService.createNotice(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> findNotice(@PathVariable("id") Long id){
        NoticeResponse response = noticeService.findNotice(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<NoticeListResponse>> findAllNotice(){
        List<NoticeListResponse> responses = noticeService.findAllNotice();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<NoticeResponse> updateNotice(
            @PathVariable("id") Long id,
            @Valid @RequestBody NoticeUpdateRequest request){
        NoticeResponse response = noticeService.updateNotice(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Long id){
        noticeService.deleteNotice(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/recentNotices")
    public ResponseEntity<List<NoticeListResponse>> getRecentNotices(){
        List<NoticeListResponse> responses = noticeService.getRecentNotices();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}
