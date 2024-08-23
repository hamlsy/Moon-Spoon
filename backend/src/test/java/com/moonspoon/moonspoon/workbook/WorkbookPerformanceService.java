package com.moonspoon.moonspoon.workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkbookPerformanceService {
    @Autowired
    private WorkbookRepository workbookRepository;


    public List<Workbook> findAll(){
        return workbookRepository.findAll();
    }
}
