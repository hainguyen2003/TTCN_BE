package com.example.ttcn2etest.request.consultingRegistration;

import com.example.ttcn2etest.model.etity.ConsultingRegistration;
import com.example.ttcn2etest.validator.EmailAnnotation;
import com.example.ttcn2etest.validator.NameAnnotation;
import com.example.ttcn2etest.validator.PhoneNumber;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.example.ttcn2etest.model.etity.ConsultingRegistration.Status.WAITING_FOR_ADVICE;

@Data
public class CreateConsultingRegistrationRequest {
    @NotBlank(message = "Họ và tên không được để trống!")
    @Size(min = 6, max = 100, message = "Họ và tên phải có ít nhất 6, nhiều nhất 100 kí tự!")
    @NameAnnotation
    private String name;

    //    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email không hợp lệ!")
    @NotBlank(message = "Email không được để trống!")
    @EmailAnnotation
    private String email;
    @NotBlank(message = "Số điện thoại không được để trống!")
    @PhoneNumber
    private String phone;
    @NotBlank(message = "Nội dung tư vấn không được để trống!")
    private String contentAdvice;

    @NotNull(message = "Trạng thái không được để trống!")
    @Enumerated(EnumType.STRING)
    private ConsultingRegistration.Status status = WAITING_FOR_ADVICE;
}
