package com.moonspoon.moonspoon.service;

import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.dto.request.WorkbookCreateRequest;
import com.moonspoon.moonspoon.dto.request.WorkbookUpdateRequest;
import com.moonspoon.moonspoon.dto.response.WorkbookResponse;
import com.moonspoon.moonspoon.repository.WorkbookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkbookService {
    private final WorkbookRepository workbookRepository;

    @Transactional
    public WorkbookResponse createWorkbook(WorkbookCreateRequest dto){
        Workbook workbook = WorkbookCreateRequest.toEntity(dto);
        workbookRepository.save(workbook);
        return WorkbookResponse.fromEntity(workbook);
    }

    public WorkbookResponse findOneById(Long id){
        Workbook workbook = workbookRepository.findById(id).orElseThrow();
        return WorkbookResponse.fromEntity(workbook);
    }

    public List<WorkbookResponse> findAll(){
        List<Workbook> workbooks = workbookRepository.findAll();
        return workbooks.stream()
                .map(w -> WorkbookResponse.fromEntity(w))
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkbookResponse updateWorkbook(Long id, WorkbookUpdateRequest dto){
        Workbook workbook = workbookRepository.findById(id).orElseThrow();
        LocalDateTime currentTime = LocalDateTime.now();
        workbook.update(dto.getTitle(), dto.getContent(), currentTime);
        return WorkbookResponse.fromEntity(workbook);
    }

    @Transactional
    public void deleteWorkbook(Long id){
        workbookRepository.deleteById(id);
    }

}
