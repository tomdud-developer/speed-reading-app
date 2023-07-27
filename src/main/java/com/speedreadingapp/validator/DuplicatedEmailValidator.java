package com.speedreadingapp.validator;

import com.speedreadingapp.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DuplicatedEmailValidator
    implements ConstraintValidator<DuplicatedEmailConstraint, String> {

  @Autowired private ApplicationUserRepository applicationUserRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (value == null || value.isEmpty()) || applicationUserRepository.findByEmail(value).isEmpty();
  }
}
