package com.src.speedreadingapp.jpa.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProgress {
    @Id
    @SequenceGenerator(
            name = "userprogress_sequence",
            sequenceName = "userprogress_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userprogress_sequence"
    )
    private Long id;

    private Boolean isBlocked;
    private Integer currentSessionNumber;
    private Integer finishedExercise;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime finishDate;

    @JsonIgnore
    @OneToOne
    private AppUser appUser;

}
