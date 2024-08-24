package com.moonspoon.moonspoon.workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class WorkbookPerformanceService {
    @Autowired
    private WorkbookRepositoryTest workbookRepositoryTest;


    public Page<Workbook> findAllVer1(String keyword, int page, int size, String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return workbookRepositoryTest.findAllVer1(keyword, pageable, username);
    }

    public Page<Workbook> findAllVer2(String keyword, int page, int size, String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return workbookRepositoryTest.findAllVer1(keyword, pageable, username);
    }
}
