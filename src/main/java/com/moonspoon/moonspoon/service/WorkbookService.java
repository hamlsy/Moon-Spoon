package com.moonspoon.moonspoon.service;

import com.moonspoon.moonspoon.domain.Workbook;
import com.moonspoon.moonspoon.repository.WorkbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkbookService {
    private final WorkbookRepository workbookRepository;

    public void createWorkbook(Workbook workbook){
        workbookRepository.save(workbook);
    }

    public Workbook findOneById(Long id){
        return workbookRepository.findById(id).orElseThrow();
    }

    public List<Workbook> findAllBy(){
        return workbookRepository.findAll();
    }

    public void updateWorkbook(Long id, Workbook updateWorkbook){
        Workbook workbook = workbookRepository.findById(id).orElseThrow();
        LocalDateTime currentTime = LocalDateTime.now();
        workbook.update(updateWorkbook.getTitle(), updateWorkbook.getContent(), currentTime);
    }
    public void deleteWorkbook(Long id){
        workbookRepository.deleteById(id);
    }

}
