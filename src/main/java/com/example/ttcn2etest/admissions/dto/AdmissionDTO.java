package com.example.ttcn2etest.admissions.dto;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionDTO {
    private Long id;
    private String title;
    private String program;
    private String description;
    private String admissionForm;
    private String image;
    private String linkRegister;
    private Boolean status;


    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp createdDate;

    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp updateDate;

    //getter + setter


}