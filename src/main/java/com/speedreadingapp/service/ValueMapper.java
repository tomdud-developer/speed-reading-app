package com.speedreadingapp.service;

import com.speedreadingapp.dto.exercise.DisappearNumbersResultResponseDTO;
import com.speedreadingapp.dto.RegisterRequestDTO;
import com.speedreadingapp.dto.exercise.SpeedMeterResultResponseDTO;
import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.entity.exercise.DisappearNumbersResult;
import com.speedreadingapp.entity.exercise.SpeedMeterResult;
import com.speedreadingapp.util.Role;

import java.time.LocalDate;

public class ValueMapper {

    private ValueMapper() {

    }

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

    public static DisappearNumbersResultResponseDTO convertEntityToResponseDTO(DisappearNumbersResult entity) {

        DisappearNumbersResultResponseDTO dto = new DisappearNumbersResultResponseDTO();
        dto.setDateTime(entity.getDateTime());
        dto.setTimeResultInSeconds(entity.getTimeResultInSeconds());
        dto.setDifficultyLevel(entity.getDifficultyLevel());

        return dto;
    }

    public static SpeedMeterResultResponseDTO convertEntityToResponseDTO(SpeedMeterResult entity) {

        SpeedMeterResultResponseDTO dto = new SpeedMeterResultResponseDTO();
        dto.setDateTime(entity.getDateTime());
        dto.setWordsPerMinute(entity.getWordsPerMinute());
        dto.setNumberOfWords(entity.getNumberOfWords());

        return dto;
    }
}
