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
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordAnnotation {
    String message() default "Mật khẩu không hợp lệ, mật khẩu phải chứa 1 ký tự viết hoa, viết thường, chữ số, 1 ký tự đặc biệt!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
