package com.speedreadingapp.dto.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeedMeterResultResponseDTO {

    private double wordsPerMinute;
    private int numberOfWords;
    private LocalDateTime dateTime;

}
