package com.speedreadingapp.dto.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeedMeterResultRequestDTO {
    private double wordsPerMinute;
    private int numberOfWords;
}
