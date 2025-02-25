package ru.effectivemobile.taskManager.annotation.priority;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PriorityValidator.class)
public @interface ValidPriority {

    String message() default "{tasks.create.errors.priority}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
