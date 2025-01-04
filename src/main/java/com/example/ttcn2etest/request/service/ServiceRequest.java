package com.example.ttcn2etest.request.service;

import com.example.ttcn2etest.model.etity.Service;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ServiceRequest {
    @NotBlank(message = "Tên dịch vụ không được để trống!")
    @Size(min = 6, max = 100, message = "Tên dịch vụ phải có ít nhất 6, nhiều nhất 100 kí tự!")
    private String name;
    //    @NotBlank(message = "Mô tả không được để trống!")
    @Size(max = 2000)
    private String description;
    //    @NotEmpty(message = "Mô tả chi tiết không được để trống!")
    @Size(max = 2000)
    private List<String> detailDescription;
    //    @NotBlank(message = "Mục tiêu đạt được không được để trống!")
    @Size(max = 300)
    private String studyGoals;
    //    @NotBlank(message = "Lịch học không được để trống!") //truong hop nguoi do khong dang ky dich vu
    @Size(max = 500, message = "Lịch học từ 0-500 ký tự!")
    private String schedule;
    //    @NotNull(message = "Số buổi không được để trống!") //truong hop nguoi do khong dang ky dich vu
    @Size(max = 100, message = "Số buổi từ 0-100 ký tự!")
    private String numberTeachingSessions;
    //    @NotEmpty(message = "Lộ trình đào tạo không được để trống!")
    @Size(max = 1000, message = "Tối đa 1000 ký tự!")
    private List<String> curriculum;

    @Enumerated(EnumType.STRING)
    private Service.Learn learnOnlineOrOffline;

    //    @NotNull(message = "Hình thức học không được để trống!")  //truong hop nguoi do khong dang ky dich vu
    @Size(max = 300, message = "Hình thức học từ 0-300 ký tự!")
    private String learningForm;
    //    @NotNull(message = "Giá tiền không được để trống!")
    private BigDecimal coursePrice;
    private String price;
    //    @NotBlank(message = "Yều cầu đầu vào của học viên không được để trống!")
    private String requestStudents;
    @NotNull(message = "Loại dịch vụ không được để trống!")
    @Enumerated(EnumType.STRING)
    private Service.TypeService typeOfService;

    //    @NotBlank(message = "Ảnh không được để trống!")
    @Size(max = 2000, message = "Link ảnh có độ dài từ 0-2000 ký tự!")
    private String image;
    //    @NotBlank(message = "Nội dung không được để trống!")
//    @Size(max = 6000, message = "Nội dung có độ dài từ 0-6000 ký tự!")
    private String content;
}
