package com.src.speedreadingapp.jpa.speedmeter;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpeedMeterLog {
        @Id
        @SequenceGenerator(
                name = "speedmeterlog_sequence",
                sequenceName = "speedmeterlog_sequence",
                allocationSize = 1
        )
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "speedmeterlog_sequence"
        )
        private Long id;
        private LocalDate date;
        private Integer wordsperminute;

        @ManyToOne
        @JsonBackReference
        private AppUser appUser;
    }
