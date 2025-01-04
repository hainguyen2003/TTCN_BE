package com.example.ttcn2etest.mocktest.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResultDTO {
    private long id ;
    private List<Integer> choiceCorrect ;

}
