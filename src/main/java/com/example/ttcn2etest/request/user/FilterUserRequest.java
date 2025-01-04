package com.example.ttcn2etest.request.user;

import com.example.ttcn2etest.validator.DateValidateAnnotation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilterUserRequest {
    @NotNull(message = "Start không được để trống")
    private Integer start;
    @NotNull(message = "Limit không được để trống")
    private Integer limit;
    @DateValidateAnnotation(message = "DateFrom phải có định dạng dd/MM/yyyy")
    private String dateFrom;
    @DateValidateAnnotation(message = "DateTo phải có định dạng dd/MM/yyyy")
    private String dateTo;
    @DateValidateAnnotation(message = "DateOfBirthFrom phải có định dạng dd/MM/yyyy")
    private String DateOfBirthFrom;
    @DateValidateAnnotation(message = "DateOfBirthTo phải có định dạng dd/MM/yyyy")
    private String DateOfBirthTo;
    private String userName;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String roleId;
    private boolean isVerified;

}
