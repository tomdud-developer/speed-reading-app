package com.speedreadingapp.dto;

import com.speedreadingapp.validator.DuplicatedEmailConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "Can't be empty")
    @Email(message = "Email is not valid")
    @DuplicatedEmailConstraint
    private String email;

    @NotBlank(message = "Can't be empty")
    private String password;

    @NotBlank(message = "Can't be empty")
    private String firstname;

    @NotBlank(message = "Can't be empty")
    private String lastname;
}
