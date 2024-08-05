package com.moonspoon.moonspoon.comment;

import com.moonspoon.moonspoon.dto.request.comment.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    //생성
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest dto){
        return null;
    }

    //수정
    @PostMapping("/update")
    public ResponseEntity<?> updateComment(){
        return null;
    }

    //전체조회
    @GetMapping("/all")
    public ResponseEntity<?> findAllComment(){
        return null;
    }

    //삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id){
        return null;
    }

}
