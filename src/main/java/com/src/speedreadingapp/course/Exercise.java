package com.src.speedreadingapp.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Getter
@Setter
@Entity(name = "exercises")
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @javax.persistence.Id
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
    private Long id;
    private String name;
    private Integer sessionNumber;
    private Integer indexInSession;
    private int param1;
    private int param2;
}
