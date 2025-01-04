package com.example.ttcn2etest.model.dto;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.etity.ExamSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamScheduleDTO {
    private long id;
    private ExamSchedule.Area areaId;
    private String nameArea;
    private String schoolId;
    private String nameExamSchool;
    private String examTime;
    private String registrationTerm;
    private String examMethod;
    private String examinationObject;
    private List<String> examinationFee;
    private List<String> examRegistrationRecords;
    private String certificationTime;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp createdDate;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp updateDate;
}
