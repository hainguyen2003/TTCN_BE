package com.example.ttcn2etest.mocktest;

import com.example.ttcn2etest.mocktest.exam.dto.DetailExamDTO;
import com.example.ttcn2etest.mocktest.exam.entity.Exam;
import com.example.ttcn2etest.mocktest.exam.request.UserTestRequest;
import com.example.ttcn2etest.mocktest.exam.service.ExamService;
import com.example.ttcn2etest.mocktest.section.dto.SectionDTO;
import com.example.ttcn2etest.mocktest.section.entity.Section;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResponseRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.service.UserResponseService;
import com.example.ttcn2etest.mocktest.user_exam.service.UserResultsService;
import com.example.ttcn2etest.response.BaseListItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/")
public class ClientController {
    private final ExamService examService;
    private final UserResponseService userResponseService;
    private final UserResultsService userResultsService;

    @GetMapping("exam/all")
    public ResponseEntity<?> getAllExamFree() {
        return ResponseEntity.ok(examService.getAllExamFree());
    }

    @GetMapping("exam/{id}")
    public ResponseEntity<?> getExamByID(@PathVariable String id) {
        DetailExamDTO exam = examService.getByID(id);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("examDetail/{id}")

    @PostMapping("exam/detail")
    public ResponseEntity<?> findSections(@RequestBody UserTestRequest request) {
        BaseListItemResponse response = new BaseListItemResponse();
        response.setSuccess(true);
        List<SectionDTO> sections = examService.findQuestionByType(request.getId(), request.getType());
        response.setResult(sections, sections.size());
        return ResponseEntity.ok(response);
    }

    @PostMapping("response/add")
    public ResponseEntity<?> addUserResponse(@RequestBody UserResponseRequest request) {
        return userResponseService.addUserResponse(request);
    }
    @GetMapping("exam/type/{type}/{isFree}")
    public ResponseEntity<?> getAllExamByType (@PathVariable String type , @PathVariable boolean isFree){
        return ResponseEntity.ok(examService.getExamByType(type , isFree));
    }
}
