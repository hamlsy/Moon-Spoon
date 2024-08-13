package com.moonspoon.moonspoon.test;

import com.moonspoon.moonspoon.dto.request.test.TestRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultRequest;
import com.moonspoon.moonspoon.dto.request.test.TestResultSubmitRequest;
import com.moonspoon.moonspoon.dto.request.test.TestSharedWorkbookRequest;
import com.moonspoon.moonspoon.dto.response.test.TestProblemResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultResponse;
import com.moonspoon.moonspoon.dto.response.test.TestResultSubmitResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/test/{id}")
public class TestController {
    private final TestService testService;


    @PostMapping("/createSharedTest")
    public ResponseEntity<List<TestProblemResponse>> getSharedTestProblem(@PathVariable("id") Long sharedWorkbookId, @RequestBody TestSharedWorkbookRequest dto){
//        List<TestProblemResponse> responses = testService.createSharedTest(sharedWorkbookId);
//        return new ResponseEntity<>(responses, HttpStatus.OK);
        return null;
    }


    @PostMapping("/getSharedTestResult")
    public ResponseEntity<List<TestResultResponse>> getSharedTestResult(@PathVariable("id") Long sharedWorkbookId
                                                                        ){
        List<TestResultResponse> responses = testService.getSharedTestResultProblem(sharedWorkbookId);
        return null;
    }

    //todo 정답률 반영 안되게
    @PostMapping("/submitSharedTestResult")
    public ResponseEntity<TestResultSubmitResponse> submitSharedTestResult(){
        return null;
    }


    @PostMapping("/storeSharedTest")
    public ResponseEntity<?> storeSharedTest(
            @PathVariable("id") Long sharedWorkbookId, @RequestBody List<TestResultRequest> listDto){

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
