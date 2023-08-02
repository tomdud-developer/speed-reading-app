package com.speedreadingapp.dto.exercise;


import com.speedreadingapp.util.DifficultyLevel;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisappearNumbersResultRequestDTO {


    private DifficultyLevel difficultyLevel;

    @DecimalMin(value = "0.001")
    private double timeResultInSeconds;

}
