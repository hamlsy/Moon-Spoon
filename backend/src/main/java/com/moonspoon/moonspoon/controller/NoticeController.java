package com.moonspoon.moonspoon.controller;

import com.moonspoon.moonspoon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/create")
    public ResponseEntity<?> createNotice(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findNotice(@PathVariable("id") Long id){
        return null;
    }


    public ResponseEntity<?> findAllNotice(){
        return null;
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateNotice(@PathVariable("id") Long id){
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotice(){
        return null;
    }

}
