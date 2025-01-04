package com.example.ttcn2etest.mocktest.answer.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AnswerRequest {
    private String questionID;
    private String answerId;
    private String answer;
    private  int key ;


}
