package com.moonspoon.moonspoon.notice;

import com.moonspoon.moonspoon.dto.request.notice.NoticeCreateRequest;
import com.moonspoon.moonspoon.dto.request.notice.NoticeUpdateRequest;
import com.moonspoon.moonspoon.dto.response.notice.NoticeListResponse;
import com.moonspoon.moonspoon.dto.response.notice.NoticeResponse;
import com.moonspoon.moonspoon.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/create")
    public ResponseEntity<NoticeResponse> createNotice(@RequestBody NoticeCreateRequest request){
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
            @RequestBody NoticeUpdateRequest request){
        
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") Long id){
        noticeService.deleteNotice(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}
