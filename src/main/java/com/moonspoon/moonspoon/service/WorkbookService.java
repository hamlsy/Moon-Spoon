package com.moonspoon.moonspoon.service;

import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.repository.WorkbookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkbookService {
    private final WorkbookRepository workbookRepository;

    @Transactional
    public Workbook createWorkbook(Workbook workbook){
        return workbookRepository.save(workbook);
    }

    public Workbook findOneById(Long id){
        return workbookRepository.findById(id).orElseThrow();
    }

    public List<Workbook> findAll(){
        return workbookRepository.findAll();
    }

    @Transactional
    public Workbook updateWorkbook(Long id, Workbook updateWorkbook){
        Workbook workbook = workbookRepository.findById(id).orElseThrow();
        LocalDateTime currentTime = LocalDateTime.now();
        workbook.update(updateWorkbook.getTitle(), updateWorkbook.getContent(), currentTime);
        return workbook;
    }

    @Transactional
    public void deleteWorkbook(Long id){
        workbookRepository.deleteById(id);
    }

}
