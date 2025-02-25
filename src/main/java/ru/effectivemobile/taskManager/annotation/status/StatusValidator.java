package ru.effectivemobile.taskManager.annotation.status;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.effectivemobile.taskManager.model.enums.task.TaskStatus;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {

    @Override
    public void initialize(ValidStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equals(value)) {
                return true;
            }
        }

        return false;
    }

}
