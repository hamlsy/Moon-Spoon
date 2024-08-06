package com.moonspoon.moonspoon.comment;

import com.moonspoon.moonspoon.dto.request.comment.CommentRequest;
import com.moonspoon.moonspoon.dto.request.comment.CommentUpdateRequest;
import com.moonspoon.moonspoon.dto.response.comment.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    //생성
    @PostMapping("/create")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest dto){
        CommentResponse response = commentService.createComment(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //수정
    @PostMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("id") Long id,
                                           @RequestBody CommentUpdateRequest request){
        CommentResponse response = commentService.updateComment(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //전체조회
    @GetMapping("/{workbookId}/all")
    public ResponseEntity<List<CommentResponse>> findAllComment(@PathVariable("workbookId") Long id){
        List<CommentResponse> response = commentService.findAllComments(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id){
        commentService.deleteComment(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
