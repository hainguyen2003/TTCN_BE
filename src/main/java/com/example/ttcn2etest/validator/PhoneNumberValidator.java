package com.example.ttcn2etest.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
// Loại bỏ các dấu cách, gạch ngang và dấu cộng nếu có
        phone = phone.replaceAll("\\s+|-|\\+", "");

        // Kiểm tra số điện thoại có độ dài từ 10 đến 15 chữ số
        if (phone.length() < 10 || phone.length() > 15) {
            return false;
        }

        // Kiểm tra số điện thoại bắt đầu bằng +84 hoặc 84
        if (phone.startsWith("84")) {
            phone = phone.substring(2);
        } else if (phone.startsWith("+84")) {
            phone = phone.substring(3);
        }

        // Kiểm tra số điện thoại có đúng đầu số 03, 05, 07, 08, 09 không
        return phone.matches("03[2-9]\\d{7}|05[6-9]\\d{7}|07[0-9]\\d{7}|08[1-9]\\d{7}|09\\d{8}");
    }
}
