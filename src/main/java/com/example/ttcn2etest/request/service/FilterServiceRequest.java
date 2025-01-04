package com.example.ttcn2etest.request.service;

import com.example.ttcn2etest.model.etity.Service;
import com.example.ttcn2etest.validator.DateValidateAnnotation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FilterServiceRequest {
    @NotNull(message = "Start không được để trống")
    private Integer start;
    @NotNull(message = "Limit không được để trống")
    private Integer limit;
    @DateValidateAnnotation(message = "DateFrom phải có định dạng dd/MM/yyyy")
    private String dateFrom;
    @DateValidateAnnotation(message = "DateTo phải có định dạng dd/MM/yyyy")
    private String dateTo;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String name;
    private Service.Learn learnOnlineOrOffline;
    private Service.TypeService typeOfService;
    private BigDecimal coursePrice;

}
