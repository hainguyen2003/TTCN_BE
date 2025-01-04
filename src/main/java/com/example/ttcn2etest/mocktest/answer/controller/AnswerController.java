package com.example.ttcn2etest.mocktest.answer.controller;

import com.example.ttcn2etest.mocktest.answer.entity.Answer;
import com.example.ttcn2etest.mocktest.answer.request.AnswerRequest;
import com.example.ttcn2etest.mocktest.answer.service.AnswerService;
import com.example.ttcn2etest.response.BaseItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/mocktest/answer/")
public class AnswerController {
    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createAnswer(@RequestBody AnswerRequest request) {
        Answer answer = answerService.createAnswer(request);
        BaseItemResponse response = new BaseItemResponse();
        response.setSuccess(true);
        response.setData(answer);
        return ResponseEntity.ok(response);
    }

    @PostMapping("update")
    public ResponseEntity<?> updateAnswer(@RequestBody AnswerRequest request) {
        Answer answer = answerService.updateAnswer(request);
        BaseItemResponse baseItemResponse = new BaseItemResponse();
        baseItemResponse.setData(answer);
        baseItemResponse.setSuccess(true);
        return ResponseEntity.ok(baseItemResponse);
    }

    @DeleteMapping("del/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable String id) {
        boolean isDelete = answerService.deleteAnswer(id);
        return ResponseEntity.ok(isDelete);
    }
}
