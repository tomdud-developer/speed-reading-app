package com.speedreadingapp.dto;


import com.speedreadingapp.util.DifficultyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DisappearNumbersResultResponseDTO {

    private DifficultyLevel difficultyLevel;
    private double timeResultInSeconds;
    private LocalDateTime dateTime;

}
