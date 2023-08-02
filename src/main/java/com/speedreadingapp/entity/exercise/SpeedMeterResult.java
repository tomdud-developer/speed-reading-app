package com.speedreadingapp.entity.exercise;

import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.util.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "speed_meter_result")
public class SpeedMeterResult {

    @Id
    @SequenceGenerator(
            name = "speed_meter_result_sequence",
            sequenceName = "speed_meter_result_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "speed_meter_result_sequence"
    )
    private long id;

    @Column(name = "words_per_minute")
    private double wordsPerMinute;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    ApplicationUser applicationUser;

}
