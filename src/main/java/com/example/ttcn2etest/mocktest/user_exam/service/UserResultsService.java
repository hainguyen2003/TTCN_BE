package com.example.ttcn2etest.mocktest.user_exam.service;

import com.example.ttcn2etest.mocktest.user_exam.request.FilterUserResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.StatisticResultsRequest;
import com.example.ttcn2etest.mocktest.user_exam.request.UserResultsRequest;
import org.springframework.http.ResponseEntity;

public interface UserResultsService {
    ResponseEntity<?> getUserResultsDetail (String id );

    ResponseEntity<?> getListUserResults (UserResultsRequest resultsRequest) ;
    ResponseEntity<?> listUserResultsByUserResponseId(String userResponseId);

    ResponseEntity<?> sortUserResultsASC(FilterUserResultsRequest request) ;
    ResponseEntity<?> sortUserResultsDESC(FilterUserResultsRequest request) ;
    ResponseEntity<?> listResultsByExam(StatisticResultsRequest request) ;
}
