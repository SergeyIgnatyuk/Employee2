package com.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom date validator
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

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
