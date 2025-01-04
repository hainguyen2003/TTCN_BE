package com.example.ttcn2etest.mocktest.question.controller;

import com.example.ttcn2etest.mocktest.question.dto.QuestionResultDTO;
import com.example.ttcn2etest.mocktest.question.request.CreateQuestionRequest;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.question.service.QuestionService;
import com.example.ttcn2etest.response.BaseItemResponse;
import com.example.ttcn2etest.response.BaseListItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mocktest/question/")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("create")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER' ,'STAFF')")
//    @PreAuthorize("hasAnyAuthority('MOD' ,'ADMIN')")
    public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionRequest req) {
        Question question = questionService.createQuestion(req);
        BaseItemResponse baseItemResponse = new BaseItemResponse();
        baseItemResponse.setSuccess(true);
        baseItemResponse.setData(question);
        return ResponseEntity.ok(baseItemResponse);
    }

    @PostMapping("update")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER' ,'STAFF')")
    public ResponseEntity<?> updateQuestion(@RequestBody CreateQuestionRequest request) {
        Question question = questionService.updateQuestion(request);
        BaseItemResponse baseItemResponse = new BaseItemResponse();
        baseItemResponse.setSuccess(true);
        baseItemResponse.setData(question);
        return ResponseEntity.ok(baseItemResponse);
    }

    @DeleteMapping("del/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER' ,'STAFF')")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id) {
        Boolean isDelete = questionService.deleteQuestion(id);
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(isDelete);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER' ,'STAFF')")
    public ResponseEntity<?> getAll() {
        List<Question> questions = questionService.listQuestion();
        BaseListItemResponse response = new BaseListItemResponse();
        response.setSuccess(true);
        response.setResult(questions, questions.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("test")
    public ResponseEntity<?> getAllResult() {
        List<QuestionResultDTO> questions = questionService.listQuestionResult();
        BaseListItemResponse response = new BaseListItemResponse();
        response.setSuccess(true);
        response.setResult(questions, questions.size());
        return ResponseEntity.ok(response);
    }
    @PostMapping("addToSection")
    @PreAuthorize("hasAnyAuthority('ADMIN' ,'TEACHER' ,'STAFF')")
    public ResponseEntity<?> addQuestionToSection (@RequestBody CreateQuestionRequest request){
        Question question = questionService.addQuestionInSection(request);
        BaseItemResponse baseItemResponse = new BaseItemResponse();
        baseItemResponse.setSuccess(true);
        baseItemResponse.setData(question);
        return ResponseEntity.ok(baseItemResponse);
    }

}
