package com.example.ttcn2etest.mocktest.exam.request;

import com.example.ttcn2etest.mocktest.section.request.SectionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExamRequest {
    private  String name ;
    private String id ;
    private String timeExam ;
    private Boolean isFree ;
    private String type ;
    List<SectionRequest> sectionRequests ;


}
