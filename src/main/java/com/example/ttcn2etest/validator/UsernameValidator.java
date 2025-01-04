package com.example.ttcn2etest.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameAnnotation, String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null) {
            return false; // Cho phép giá trị null
        }

//        // Kiểm tra độ dài tối thiểu là 5 ký tự
//        if (username.length() < 6) {
//            return false;
//        }

        // Kiểm tra username không chứa khoảng trắng
        if (username.contains(" ")) {
            return false;
        }

        // Kiểm tra username chỉ chứa các ký tự chữ cái (viết hoa hoặc thường),
        // số và dấu chấm, và không chứa các thuật ngữ chung hoặc phần mở rộng
        String usernamePattern = "^[a-zA-Z0-9.]*$";
        if (!username.matches(usernamePattern)) {
            return false;
        }

        // Kiểm tra username không chứa .com hoặc .net
        if (username.contains(".com") || username.contains(".net")) {
            return false;
        }

        // Nếu tất cả các điều kiện đều thỏa mãn, trả về true
        return true;
    }
}
