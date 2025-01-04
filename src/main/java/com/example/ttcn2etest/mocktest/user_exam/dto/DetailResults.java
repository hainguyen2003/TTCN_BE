package com.example.ttcn2etest.mocktest.user_exam.dto;

import com.example.ttcn2etest.mocktest.question.dto.QuestionDTO;
import com.example.ttcn2etest.mocktest.question.entity.Question;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder

public class DetailResults {
    private QuestionDTO question ;
    private List<Integer> choiceCorrect ;
    private List<Integer> choiceUser ;


}
