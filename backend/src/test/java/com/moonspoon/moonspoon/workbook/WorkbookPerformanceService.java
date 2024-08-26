package com.moonspoon.moonspoon.workbook;

import com.moonspoon.moonspoon.dto.response.WorkbookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkbookPerformanceService {
    @Autowired
    private WorkbookRepositoryTest workbookRepositoryTest;


    public Page<Workbook> findAllVer1(String keyword, int page, int size, String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Workbook> workbooks = workbookRepositoryTest.findAllVer1(keyword, pageable, username);
        Page<Workbook> responses = workbooks.map(w -> Workbook.builder()
                .problemCount(w.getProblems().size())
                .build());

        return responses;
    }

    public Page<Workbook> findAllVer2(String keyword, int page, int size, String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Workbook> workbooks = workbookRepositoryTest.findAllVer2(keyword, pageable, username);
        List<Long> workbookIds = workbooks.stream()
                .map(Workbook::getId)
                .collect(Collectors.toList());
        List<WorkbookProblemCountTestDto> problemCounts = workbookRepositoryTest.countProblemsByWorkbookId(workbookIds);

        Map<Long, Long> problemCountMap = problemCounts.stream()
                .collect(Collectors.toMap(WorkbookProblemCountTestDto::getWorkbookId, WorkbookProblemCountTestDto::getProblemCount));

        Page<Workbook> responses = workbooks.map(w -> Workbook.builder()
                .problemCount(problemCountMap.get(w.getId()).intValue())
                .build());

        return responses;
    }

    public Page<Workbook> findAllVer3(String keyword, int page, int size, String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("create_date").descending());
        Page<Workbook> workbooks = workbookRepositoryTest.findAllVer3(keyword, pageable, username);
        List<Long> workbookIds = workbooks.stream()
                .map(Workbook::getId)
                .collect(Collectors.toList());
        List<WorkbookProblemCountTestDto> problemCounts = workbookRepositoryTest.countProblemsByWorkbookId(workbookIds);

        Map<Long, Long> problemCountMap = problemCounts.stream()
                .collect(Collectors.toMap(WorkbookProblemCountTestDto::getWorkbookId, WorkbookProblemCountTestDto::getProblemCount));

        Page<Workbook> responses = workbooks.map(w -> Workbook.builder()
                .problemCount(problemCountMap.get(w.getId()).intValue())
                .build());

        return responses;
    }

    @Cacheable(value = "workbooks", key="#username + '_' + #keyword + '_' + #page + '_' + #size")
    public Page<Workbook> findAllVer4(String keyword, int page, int size, String username){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Workbook> workbooks = workbookRepositoryTest.findAllVer4(keyword, pageable, username);
        List<Long> workbookIds = workbooks.stream()
                .map(Workbook::getId)
                .collect(Collectors.toList());
        List<WorkbookProblemCountTestDto> problemCounts = workbookRepositoryTest.countProblemsByWorkbookId(workbookIds);

        Map<Long, Long> problemCountMap = problemCounts.stream()
                .collect(Collectors.toMap(WorkbookProblemCountTestDto::getWorkbookId, WorkbookProblemCountTestDto::getProblemCount));

        Page<Workbook> responses = workbooks.map(w -> Workbook.builder()
                .problemCount(problemCountMap.get(w.getId()).intValue())
                .build());

        return responses;
    }

}
