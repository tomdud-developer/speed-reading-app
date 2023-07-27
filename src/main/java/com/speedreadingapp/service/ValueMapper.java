package com.speedreadingapp.service;

import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.util.Role;

import java.time.LocalDate;

public class ValueMapper {

    public static ApplicationUser convertToEntity(RegisterRequestDTO registerRequestDTO) {

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setEmail(registerRequestDTO.getEmail());
        applicationUser.setPassword(registerRequestDTO.getPassword());
        applicationUser.setFirstname(registerRequestDTO.getFirstname());
        applicationUser.setLastname(registerRequestDTO.getLastname());
        applicationUser.setAccountCreationDate(LocalDate.now());
        applicationUser.setEnabled(true);
        applicationUser.getRoles().add(Role.USER);

        return applicationUser;
    }
}
