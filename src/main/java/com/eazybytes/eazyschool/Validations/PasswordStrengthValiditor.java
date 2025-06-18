package com.eazybytes.eazyschool.Validations;

import com.eazybytes.eazyschool.Annotations.FieldsValueMatch;
import com.eazybytes.eazyschool.Annotations.PasswordValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class PasswordStrengthValiditor implements ConstraintValidator<PasswordValidator, String> {

    List<String> weakPassword;


    @Override
    public void initialize(PasswordValidator passwordValidator) {
        weakPassword= Arrays.asList("1234", "Password","123456786");
    }

    @Override
    public boolean isValid(String passwordField, ConstraintValidatorContext cxt) {
        return passwordField != null && (!weakPassword.contains(passwordField));
    }
}
