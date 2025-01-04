package com.example.ttcn2etest.mocktest.user_exam.request;

import lombok.Data;

@Data
public class StatisticResultsRequest {
    private String examId ;
    private boolean sortHighToLow ;
    private boolean sortCreateDate ;
}
