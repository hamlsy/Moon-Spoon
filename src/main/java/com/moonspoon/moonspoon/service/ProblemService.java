package com.moonspoon.moonspoon.service;

import com.moonspoon.moonspoon.domain.Problem;
import com.moonspoon.moonspoon.domain.User;
import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.dto.request.ProblemCreateRequest;
import com.moonspoon.moonspoon.dto.response.ProblemResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.exception.NotUserException;
import com.moonspoon.moonspoon.repository.ProblemRepository;
import com.moonspoon.moonspoon.repository.UserRepository;
import com.moonspoon.moonspoon.repository.WorkbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final WorkbookRepository workbookRepository;
    private final UserRepository userRepository;

    public ProblemResponse create(ProblemCreateRequest dto, Long workbookId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        validateUser(username);

        //문제집 예외
        Workbook workbook = workbookRepository.findById(workbookId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 문제집입니다.")
        );
        //사용자 예외
        if(!workbook.getUser().getUsername().equals(username)){
            throw new NotUserException("권한이 없습니다.");
        }

        Problem problem = ProblemCreateRequest.toEntitu(dto);
        User user = userRepository.findByUsername(username);

        problem.setUser(user);
        problem.setWorkbook(workbook);
        problem.setCreateDate(LocalDateTime.now());

        problemRepository.save(problem);
        return ProblemResponse.fromEntity(problem);
    }
    private void validateUser(String username) {
        if(username == null || username.equals("anonymousUser")){
            throw new NotUserException("권한이 없습니다.");
        }
    }
}
