package com.example.ttcn2etest.mocktest.exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamDTO {
    private String id ;
    private String name ;
    private String type ;
    private String timeExam ;
    private Boolean isFree;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String createDate ;
}
