package com.src.speedreadingapp.jpa.understandingmeter.textquestions;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "understandinglevelquestion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnderstandingLevelQuestion {
    @Id
    @SequenceGenerator(
            name = "understandinglevelquestion_sequence",
            sequenceName = "understandinglevelquestion_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "understandinglevelquestion_sequence"
    )
    private Long id;
    private Integer index;
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;

    private Character correctAnswer;

    @JsonIgnore
    @ManyToOne
    private UnderstandingLevelText understandingLevelText;

}
