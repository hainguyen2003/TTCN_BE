package com.example.ttcn2etest.mocktest.answer.service;

import com.example.ttcn2etest.mocktest.answer.entity.Answer;
import com.example.ttcn2etest.mocktest.answer.request.AnswerRequest;

import java.util.UUID;

public interface AnswerService {
    Answer createAnswer(AnswerRequest request);
    Answer updateAnswer(AnswerRequest request);

    boolean deleteAnswer(String id);

}
