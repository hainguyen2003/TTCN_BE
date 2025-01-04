package com.example.ttcn2etest.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NameValidator implements ConstraintValidator<NameAnnotation, String> {
    private final static String NAME_REGEX = "^[^<>{}\\\\\\\"|;:.,~!?@#$%^=&*\\\\\\\\)(]*$";
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if(name == null){
            return true;
        }
        return Pattern.compile(NAME_REGEX).matcher(name).matches();
    }
}
