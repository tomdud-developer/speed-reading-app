package com.speedreadingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "session")
public class Session {

    @Id
    @SequenceGenerator(
            name = "session_sequence",
            sequenceName = "session_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "session_sequence"
    )
    @Immutable
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrainingPlan trainingPlan;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private Set<Exercise> exercises = new HashSet<>();

}
