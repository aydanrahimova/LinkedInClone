package az.matrix.linkedinclone.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidDateRange {
    String message() default "Start date must be before the end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String startField();
    String endField();
}

