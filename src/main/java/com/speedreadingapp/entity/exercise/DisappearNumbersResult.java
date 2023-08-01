package com.speedreadingapp.entity.exercise;

import com.speedreadingapp.entity.ApplicationUser;
import com.speedreadingapp.util.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "disappear_number_result")
public class DisappearNumbersResult {

    @Id
    @SequenceGenerator(
            name = "disappear_number_result_sequence",
            sequenceName = "disappear_number_result_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "disappear_number_result_sequence"
    )
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel;

    @Column(name = "time_resul_in_seconds")
    private double timeResultInSeconds;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    ApplicationUser applicationUser;

}
