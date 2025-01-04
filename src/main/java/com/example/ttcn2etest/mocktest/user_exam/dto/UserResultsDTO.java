package com.example.ttcn2etest.mocktest.user_exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResultsDTO {
    private String id;

    private float pointReading ;
    private float pointListening ;
    private float pointWriting ;
    private float pointSpeaking ;
    private float totalPoint ;
    private String comment;
    private String nameExam ;
    private String time ;
    private String key ;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm" ,timezone = "GMT+7")
    private Timestamp createDate;

    private List<DetailResults> detailResults;


}
