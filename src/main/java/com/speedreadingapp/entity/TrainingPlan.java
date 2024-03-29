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
@Table(name = "training_plan")
public class TrainingPlan {
    @Id
    @SequenceGenerator(
            name = "training_plan_sequence",
            sequenceName = "training_plan_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "training_plan_sequence"
    )
    @Immutable
    private long id;

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL)
    private Set<Session> sessions = new HashSet<>();

}
