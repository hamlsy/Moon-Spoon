package com.moonspoon.moonspoon.sharedWorkbook;

import com.moonspoon.moonspoon.dto.request.sharedWorkbook.SharedWorkbookRequest;
import com.moonspoon.moonspoon.dto.response.sharedWorkbook.SharedWorkbookResponse;
import com.moonspoon.moonspoon.exception.NotFoundException;
import com.moonspoon.moonspoon.workbook.Workbook;
import com.moonspoon.moonspoon.workbook.WorkbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
public class SharedWorkbookServiceTest {
    @Autowired
    private SharedWorkbookService sharedWorkbookService;

    @Autowired
    private SharedWorkbookRepository sharedWorkbookRepository;

    @Autowired
    private WorkbookRepository workbookRepository;

    private Long workbookId;

    @BeforeEach
    public void setUp(){
        Workbook workbook = new Workbook();
        workbook.setTitle("w1 title");
        workbook.setContent("w1 content");
        workbook.setAuthor("w1 author");
        Workbook saveWorkbook = workbookRepository.save(workbook);

        this.workbookId = saveWorkbook.getId();
        System.out.println("workbook id is " + workbookId);
    }

    //등록
    @Test
    @DisplayName("문제집 공유 테스트")
    void sharedWorkbookTest(){
        //given

        SharedWorkbookRequest dto = new SharedWorkbookRequest();
        dto.setTitle("s1");
        dto.setContent("s1");
        dto.setWorkbookId(workbookId);

        //when
        SharedWorkbookResponse response =  sharedWorkbookService.sharedWorkbook(dto);

        //then
        SharedWorkbook sharedWorkbook = sharedWorkbookRepository.findById(response.getId()).orElseThrow(
                () -> new NotFoundException("Not Found")
        );

        assertNotNull(sharedWorkbook, "The Object is null");

    }
}
