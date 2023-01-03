package com.src.speedreadingapp.jpa.columnnumberexerciselogs;

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
public class ColumnNumberExerciseLog {
    @Id
    @SequenceGenerator(
            name = "columnnumberexerciselog",
            sequenceName = "columnnumberexerciselog_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "columnnumberexerciselog_sequence"
    )
    private Long id;

    @Column(columnDefinition = "integer default -1") private Integer log2;
    @Column(columnDefinition = "integer default -1") private Integer log3;
    @Column(columnDefinition = "integer default -1") private Integer log4;
    @Column(columnDefinition = "integer default -1") private Integer log5;
    @Column(columnDefinition = "integer default -1") private Integer log6;

    @JsonIgnore
    @OneToOne
    private AppUser appUser;

    public Integer[] getArray() {
        Integer [] array = new Integer[5];
        array[0] = log2;
        array[1] = log3;
        array[2] = log4;
        array[3] = log5;
        array[4] = log6;

        return array;
    }

    public void setArray(Integer[] array) {
        if( array[0] != null )
            log2 = array[0];
        if( array[1] != null )
            log3 = array[1];
        if( array[2] != null )
            log4 = array[2];
        if( array[3] != null )
            log5 = array[3];
        if( array[4] != null )
            log6 = array[4];
    }
}