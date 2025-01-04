package com.example.ttcn2etest.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailAnnotation, String> {
    private final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void initialize(EmailAnnotation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) {
            return true; // Allow null values
        }

        // Kiểm tra độ dài tổng cộng của email không vượt quá 320 ký tự
        if (email.length() > 320) {
            return false;
        }

        // Kiểm tra xem email có chứa đúng một ký tự '@'
        int atCount = (int) email.chars().filter(ch -> ch == '@').count();
        if (atCount != 1) {
            return false;
        }

        // Kiểm tra xem email có bắt đầu hoặc kết thúc bằng dấu chấm (".")
        if (email.startsWith(".") || email.endsWith(".")) {
            return false;
        }

        // Kiểm tra không có hai hoặc nhiều dấu chấm liên tiếp, ".@" hoặc "@."
        if (email.contains("..") || email.contains(".@") || email.contains("@.")) {
            return false;
        }

        // Kiểm tra độ dài tên miền không vượt quá 254 ký tự
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        String domain = parts[1];
        if (domain.length() > 254) {
            return false;
        }

        // Kiểm tra định dạng email bằng regex
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
