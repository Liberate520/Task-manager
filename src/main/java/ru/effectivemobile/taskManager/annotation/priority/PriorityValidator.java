package ru.effectivemobile.taskManager.annotation.priority;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.effectivemobile.taskManager.model.enums.task.TaskPriority;

public class PriorityValidator implements ConstraintValidator<ValidPriority, String> {

    @Override
    public void initialize(ValidPriority constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (TaskPriority priority : TaskPriority.values()) {
            if (priority.name().equals(value)) {
                return true;
            }
        }

        return false;
    }
}
