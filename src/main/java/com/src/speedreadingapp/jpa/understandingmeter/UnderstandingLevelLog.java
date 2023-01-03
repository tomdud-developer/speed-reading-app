package com.src.speedreadingapp.jpa.understandingmeter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnderstandingLevelLog {
    @Id
    @SequenceGenerator(
            name = "understandinglevellog_sequence",
            sequenceName = "understandinglevellog_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "understandinglevellog_sequence"
    )
    private Long id;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;
    private Integer percentageOfUnderstanding;

    @JsonIgnore
    @OneToOne
    private AppUser appUser;
}
