package com.example.ttcn2etest.mocktest.question.service;

import com.example.ttcn2etest.mocktest.question.dto.QuestionResultDTO;
import com.example.ttcn2etest.mocktest.question.request.CreateQuestionRequest;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import com.example.ttcn2etest.mocktest.section.entity.Section;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    Question createQuestion(CreateQuestionRequest request);
    Question updateQuestion(CreateQuestionRequest request);
    boolean deleteQuestion(String id);

    List<Question> listQuestion();


    Question createQuestionInSection (CreateQuestionRequest request , Section section);

    List<QuestionResultDTO> listQuestionResult ();
    Question addQuestionInSection(CreateQuestionRequest request);

}
