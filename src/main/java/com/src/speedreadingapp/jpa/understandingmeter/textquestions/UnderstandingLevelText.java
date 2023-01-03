package com.src.speedreadingapp.jpa.understandingmeter.textquestions;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import com.src.speedreadingapp.jpa.speedmeter.SpeedMeterLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnderstandingLevelText {
    @Id
    @SequenceGenerator(
            name = "understandingleveltext_sequence",
            sequenceName = "understandingleveltext_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "understandingleveltext_sequence"
    )
    private Long id;

    private Integer index;

    @Lob
    @Column
    private String text;


    public String getContent() {
        return text;
    }



}
