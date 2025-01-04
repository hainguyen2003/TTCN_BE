package com.example.ttcn2etest.mocktest.user_exam.request;

import lombok.Data;

@Data
public class FilterUserResponseRequest {
    private String email ;
    private String examName ;
    private boolean isSortCountExam ;
}
