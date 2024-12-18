package az.matrix.linkedinclone.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Class<?> clazz = value.getClass();
            Field startField = clazz.getDeclaredField(this.startField);
            Field endField = clazz.getDeclaredField(this.endField);

            startField.setAccessible(true);
            endField.setAccessible(true);

            Object startValue = startField.get(value);
            Object endValue = endField.get(value);

            if (startValue == null || endValue == null) {
                return true;
            }

            if (startValue instanceof Comparable && endValue instanceof Comparable) {
                return ((Comparable) startValue).compareTo(endValue) < 0;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
