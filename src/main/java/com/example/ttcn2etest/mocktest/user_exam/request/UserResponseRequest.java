package com.example.ttcn2etest.mocktest.user_exam.request;

import com.example.ttcn2etest.mocktest.user_exam.entity.UserResults;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserResponseRequest {
    private String id ;
    private int totalPoint ;
    private int count ;
    private String exam_id;
    private Long user_id ;
    private String email ;

    private int maxCount ;

    List<UserResultsRequest> responselistening ;
    List<UserResultsRequest> responsereading ;
    List<UserResultsRequest> responsespeaking ;
    List<UserResultsRequest> responsewriting ;


//    private String responseUsers ;
}
