package com.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateValidator implements ConstraintValidator<CustomDateConstraint, Date> {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public void initialize(CustomDateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return true;
    }
}
