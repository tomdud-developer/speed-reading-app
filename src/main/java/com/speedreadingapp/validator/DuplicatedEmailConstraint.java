package com.speedreadingapp.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = DuplicatedEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedEmailConstraint {
  String message() default "This email is already exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
