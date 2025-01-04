package com.example.ttcn2etest.mocktest.user_exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "GMT+7")
    private Date createTime;
    private int totalPoint;
    private int count ;
    private int maxCount;
    private String examName;
    private String userName;
    private String email;
    private long userId;


}
