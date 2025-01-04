package com.example.ttcn2etest.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
//@Target({ElementType.FIELD, ElementType.METHOD,ElementType.PARAMETER})
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailAnnotation {
    String message() default "Email không đúng định dang!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
