package com.example.ttcn2etest.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
public @interface UsernameAnnotation {
    String message() default "Tên đăng nhập(Username) không hợp lệ, không được chứa khoảng trắng, viết liền và có thể chứa ký tự là chữ và số!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
