package com.example.ttcn2etest.request.news;

import com.example.ttcn2etest.validator.DateValidateAnnotation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterNewsRequest {
    @NotNull(message = "Start không được để trống")
    private Integer start;
    @NotNull(message = "Limit không được để trống")
    private Integer limit;
    @DateValidateAnnotation(message = "DateFrom phải có định dạng dd/MM/yyyy")
    private String dateFrom;
    @DateValidateAnnotation(message = "DateTo phải có định dạng dd/MM/yyyy")
    private String dateTo;
    private String name;
}
