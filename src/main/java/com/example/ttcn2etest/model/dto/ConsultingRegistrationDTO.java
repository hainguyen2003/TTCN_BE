package com.example.ttcn2etest.model.dto;

import com.example.ttcn2etest.constant.DateTimeConstant;
import com.example.ttcn2etest.model.etity.ConsultingRegistration;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsultingRegistrationDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String contentAdvice;
    private ConsultingRegistration.Status status;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp createdDate;
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_FORMAT, timezone = DateTimeConstant.TIME_ZONE)
    private Timestamp updateDate;
}
