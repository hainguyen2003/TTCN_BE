package com.example.ttcn2etest.mocktest.question.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionRequest {
    private String id ;
    private String content ;
    private String type ;
    private String description ;
    private float point ;
    private String section_id ;

    private List<AnswerReq> listAnswer ;

    private List<Integer> choiceCorrect ;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder

    public  static class AnswerReq{
        private String answer;
    }

}
