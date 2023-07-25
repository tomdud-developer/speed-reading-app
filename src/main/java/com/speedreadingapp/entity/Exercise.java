package com.speedreadingapp.entity;

import com.speedreadingapp.util.DifficultyLevel;
import com.speedreadingapp.util.ExerciseName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "exercise")
public class Exercise {

    @Id
    @SequenceGenerator(
            name = "exercise_sequence",
            sequenceName = "exercise_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "exercise_sequence"
    )
    @Immutable
    private long id;

    @Enumerated(EnumType.STRING)
    private ExerciseName exerciseName;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;
}
