package com.moonspoon.moonspoon.comment;

import com.moonspoon.moonspoon.dto.request.comment.CommentRequest;
import com.moonspoon.moonspoon.dto.request.comment.CommentUpdateRequest;
import com.moonspoon.moonspoon.dto.response.comment.CommentResponse;

import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbook;
import com.moonspoon.moonspoon.sharedWorkbook.SharedWorkbookRepository;
import com.moonspoon.moonspoon.user.User;
import com.moonspoon.moonspoon.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final SharedWorkbookRepository sharedWorkbookRepository;
    private final String notFoundWorkbookMessage = "존재하지 않는 문제집입니다.";
    private final UserRepository userRepository;

    //생성
    @Transactional
    public CommentResponse createComment(CommentRequest dto){

        SharedWorkbook sharedWorkbook = findSharedWorkbookById(dto.getSharedWorkbookId());

        Comment comment = CommentRequest.toEntity(dto);
        comment.setCreateDate(LocalDateTime.now());
        comment.setSharedWorkbook(sharedWorkbook);

        User user = findCurrentUser();

        comment.setUser(user);
        comment.setAuthor(user.getName());

        commentRepository.save(comment);

        CommentResponse response = CommentResponse.fromEntity(comment);

        return response;
    }
    //수정
    @Transactional
    public CommentResponse updateComment(CommentUpdateRequest dto){
        Comment comment = commentRepository.findById(dto.getId())
                .orElseThrow(
                        () -> new NotFoundException("존재하지 않는 댓글입니다.")
                );
        comment.updateComment(dto.getContent(), LocalDateTime.now());

        CommentResponse response = CommentResponse.fromEntity(comment);
        return response;
    }

    //전체조회
    public List<CommentResponse> findAllComments(Long sharedWorkbookId){
        List<Comment> comments = sharedWorkbookRepository.findByIdWithComments(sharedWorkbookId)
                .orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                ).getComments();

        List<CommentResponse> responses = comments.stream()
                .map(c -> CommentResponse.fromEntity(c))
                .collect(Collectors.toList());
        return responses;
    }

    //삭제
    @Transactional
    public void deleteComment(Long id){
        Comment comment = findCommentByIdWithUser(id);
        String username = comment.getUser().getUsername();
        validUser(username);

        commentRepository.delete(comment);

    }


    private void validUser(String username){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!currentUser.equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }
    }

    private User findCurrentUser(){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser);
        if(user == null){
            throw new NotUserException("권한이 없습니다.");
        }
        return user;
    }

    private SharedWorkbook findSharedWorkbookById(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findById(
                id).orElseThrow(
                () -> new NotFoundException(notFoundWorkbookMessage)
        );
        return sharedWorkbook;
    }

    private Comment findCommentByIdWithUser(Long id){
        Comment comment = commentRepository.findByIdWithUser(id)
                .orElseThrow(
                        () -> new NotFoundException("존재하지 않는 댓글입니다.")
                );
        return comment;
    }
}
