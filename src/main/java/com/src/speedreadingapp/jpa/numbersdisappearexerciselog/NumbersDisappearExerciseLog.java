package com.src.speedreadingapp.jpa.numbersdisappearexerciselog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.src.speedreadingapp.jpa.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NumbersDisappearExerciseLog {
    @Id
    @SequenceGenerator(
            name = "numbersdisappearexerciselog",
            sequenceName = "numbersdisappearexerciselog_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "numbersdisappearexerciselog_sequence"
    )
    private Long id;

    @Column(columnDefinition = "integer default -1") private Integer log0;
    @Column(columnDefinition = "integer default -1") private Integer log1;
    @Column(columnDefinition = "integer default -1") private Integer log2;
    @Column(columnDefinition = "integer default -1") private Integer log3;
    @Column(columnDefinition = "integer default -1") private Integer log4;
    @Column(columnDefinition = "integer default -1") private Integer log5;
    @Column(columnDefinition = "integer default -1") private Integer log6;
    @Column(columnDefinition = "integer default -1") private Integer log7;
    @Column(columnDefinition = "integer default -1") private Integer log8;
    @Column(columnDefinition = "integer default -1") private Integer log9;
    @Column(columnDefinition = "integer default -1") private Integer log10;

    @JsonIgnore
    @OneToOne
    private AppUser appUser;

    public Integer[] getArray() {
        Integer [] array = new Integer[11];
        array[0] = log0;
        array[1] = log1;
        array[2] = log2;
        array[3] = log3;
        array[4] = log4;
        array[5] = log5;
        array[6] = log6;
        array[7] = log7;
        array[8] = log8;
        array[9] = log9;
        array[10] = log10;

        return array;
    }

    public void setArray(Integer[] array) {
        if( array[0] != null )
            log0 = array[0];
        if( array[1] != null )
            log1 = array[1];
        if( array[2] != null )
            log2 = array[2];
        if( array[3] != null )
            log3 = array[3];
        if( array[4] != null )
            log4 = array[4];
        if( array[5] != null )
            log5 = array[5];
        if( array[6] != null )
            log6 = array[6];
        if( array[7] != null )
            log7 = array[7];
        if( array[8] != null )
            log8 = array[8];
        if( array[9] != null )
            log9 = array[9];
        if( array[10] != null )
            log10 = array[10];
    }
}