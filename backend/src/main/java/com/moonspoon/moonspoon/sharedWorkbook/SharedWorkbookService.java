package com.moonspoon.moonspoon.sharedWorkbook;

import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookRequest;
import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookGetUserResponse;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookResponse;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookTestResponse;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.problem.Problem;
import com.moonspoon.moonspoon.user.UserRepository;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.workbook.WorkbookRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharedWorkbookService {
    private final SharedWorkbookRepository sharedWorkbookRepository;
    private final WorkbookRepository workbookRepository;
    private final String notFoundWorkbookMessage = "존재하지 않는 문제집입니다.";


    //단일 조회
    public SharedWorkbookResponse findSharedWorkbook(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findById(id).orElseThrow(
                () -> new NotFoundException(notFoundWorkbookMessage)
        );
        SharedWorkbookResponse response = SharedWorkbookResponse.fromEntity(sharedWorkbook);
        return response;
    }

    //전체 조회
    public List<SharedWorkbookResponse> findAllSharedWorkbook(){
        List<SharedWorkbook> sharedWorkbooks = sharedWorkbookRepository.findAll();
        List<SharedWorkbookResponse> responses = sharedWorkbooks.stream()
                .map(s -> SharedWorkbookResponse.fromEntity(s))
                .collect(Collectors.toList());
        return responses;
    }


    //등록
    @Transactional
    public SharedWorkbookResponse sharedWorkbook(SharedWorkbookRequest dto){
        SharedWorkbook sharedWorkbook = SharedWorkbookRequest.toEntity(dto);
        Workbook workbook = workbookRepository.findByIdWithUser(dto.getWorkbookId()).orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                );
        sharedWorkbook.setSharedDate(LocalDateTime.now());
        sharedWorkbook.setWorkbook(workbook);
        sharedWorkbook.setAuthor(workbook.getAuthor());
        sharedWorkbook.setUser(workbook.getUser());


        sharedWorkbookRepository.save(sharedWorkbook);

        SharedWorkbookResponse response = SharedWorkbookResponse.fromEntity(sharedWorkbook);
        return response;
    }

    //수정
    @Transactional
    public SharedWorkbookResponse updateSharedWorkbook(Long id, SharedWorkbookUpdateRequest dto){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithUser(id)
                .orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                );
        validUser(sharedWorkbook.getUser().getUsername());

        sharedWorkbook.updateSharedWorkbook(dto.getTitle(), dto.getContent(),
                dto.isRandom(), dto.isHasSolution(), LocalDateTime.now());

        SharedWorkbookResponse response = SharedWorkbookResponse.fromEntity(sharedWorkbook);
        return response;
    }


    //삭제
    @Transactional
    public void deleteSharedWorkbook(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithUser(id)
                .orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                );
        validUser(sharedWorkbook.getUser().getUsername());
        sharedWorkbookRepository.deleteById(id);
    }


    private void validUser(String username){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!currentUser.equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }
    }

    public List<SharedWorkbookTestResponse> testSharedWorkbook(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithWorkbookAndProblems(id)
                .orElseThrow(
                        () -> new NotFoundException(notFoundWorkbookMessage)
                );
        Workbook workbook = sharedWorkbook.getWorkbook();
        List<Problem> problems = workbook.getProblems();
        List<SharedWorkbookTestResponse> responses = problems.stream()
                .map(p -> SharedWorkbookTestResponse.fromEntity(p))
                .collect(Collectors.toList());
        return responses;
    }

    //유저 검증
    public SharedWorkbookGetUserResponse getUser(Long id){
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findByIdWithUser(id).orElseThrow(
                () -> new NotFoundException(notFoundWorkbookMessage)
        );
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        SharedWorkbookGetUserResponse response = new SharedWorkbookGetUserResponse();

        if(currentUser.equals(sharedWorkbook.getUser().getUsername())){
            response.setUser(true);
        }else{
            response.setUser(false);
        }
        return response;
    }

}
