package com.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Custom annotation for Validator
 *
 * @author Sergey Ignatyuk
 * @version 1.0
 */

@Documented
@Constraint(validatedBy = CustomDateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomDateConstraint {
    String message() default "Invalid date format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
