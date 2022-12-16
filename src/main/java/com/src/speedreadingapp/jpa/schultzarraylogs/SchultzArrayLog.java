package com.src.speedreadingapp.jpa.schultzarraylogs;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class SchultzArrayLog {
    @Id
    @SequenceGenerator(
            name = "schultzarraylogs",
            sequenceName = "schultzarraylogs",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "schultzarraylogs_sequence"
    )
    private Long id;

    @Column(columnDefinition = "integer default -1") private Integer log2x2;
    @Column(columnDefinition = "integer default -1") private Integer log2x3;
    @Column(columnDefinition = "integer default -1") private Integer log2x4;
    @Column(columnDefinition = "integer default -1") private Integer log2x5;
    @Column(columnDefinition = "integer default -1") private Integer log2x6;

    @Column(columnDefinition = "integer default -1") private Integer log3x2;
    @Column(columnDefinition = "integer default -1") private Integer log3x3;
    @Column(columnDefinition = "integer default -1") private Integer log3x4;
    @Column(columnDefinition = "integer default -1") private Integer log3x5;
    @Column(columnDefinition = "integer default -1") private Integer log3x6;

    @Column(columnDefinition = "integer default -1") private Integer log4x2;
    @Column(columnDefinition = "integer default -1") private Integer log4x3;
    @Column(columnDefinition = "integer default -1") private Integer log4x4;
    @Column(columnDefinition = "integer default -1") private Integer log4x5;
    @Column(columnDefinition = "integer default -1") private Integer log4x6;

    @Column(columnDefinition = "integer default -1") private Integer log5x2;
    @Column(columnDefinition = "integer default -1") private Integer log5x3;
    @Column(columnDefinition = "integer default -1") private Integer log5x4;
    @Column(columnDefinition = "integer default -1") private Integer log5x5;
    @Column(columnDefinition = "integer default -1") private Integer log5x6;

    @Column(columnDefinition = "integer default -1") private Integer log6x2;
    @Column(columnDefinition = "integer default -1") private Integer log6x3;
    @Column(columnDefinition = "integer default -1") private Integer log6x4;
    @Column(columnDefinition = "integer default -1") private Integer log6x5;
    @Column(columnDefinition = "integer default -1") private Integer log6x6;


    protected Integer[][] getArray() {

        Integer [][] array = new Integer[5][5];

        array[0][0] = log2x2;
        array[0][1] = log2x3;
        array[0][2] = log2x4;
        array[0][3] = log2x5;
        array[0][4] = log2x6;

        array[1][0] = log3x2;
        array[1][1] = log3x3;
        array[1][2] = log3x4;
        array[1][3] = log3x5;
        array[1][4] = log3x6;

        array[2][0] = log4x2;
        array[2][1] = log4x3;
        array[2][2] = log4x4;
        array[2][3] = log4x5;
        array[2][4] = log4x6;

        array[3][0] = log5x2;
        array[3][1] = log5x3;
        array[3][2] = log5x4;
        array[3][3] = log5x5;
        array[3][4] = log5x6;

        array[4][0] = log6x2;
        array[4][1] = log6x3;
        array[4][2] = log6x4;
        array[4][3] = log6x5;
        array[4][4] = log6x6;

        return array;
    }

    protected Integer[][] setArray(Integer[][] array) {

        if( array[0][0] != null )
            log2x2 = array[0][0];
        if( array[0][1] != null )
            log2x3 = array[0][1];
        if( array[0][2] != null )
            log2x4 = array[0][2];
        if( array[0][3] != null )
            log2x5 = array[0][3];
        if( array[0][4] != null )
            log2x6 = array[0][4];

        if( array[1][0] != null )
            log3x2 = array[1][0];
        if( array[1][1] != null )
            log3x3 = array[1][1];
        if( array[1][2] != null )
            log3x4 = array[1][2];
        if( array[1][3] != null )
            log3x5 = array[1][3];
        if( array[1][4] != null )
            log3x6 = array[1][4];

        if( array[2][0] != null )
            log4x2 = array[2][0];
        if( array[2][1] != null )
            log4x3 = array[2][1];
        if( array[2][2] != null )
            log4x4 = array[2][2];
        if( array[2][3] != null )
            log4x5 = array[2][3];
        if( array[2][4] != null )
            log4x6 = array[2][4];

        if( array[3][0] != null )
            log5x2 = array[3][0];
        if( array[3][1] != null )
            log5x3 = array[3][1];
        if( array[3][2] != null )
            log5x4 = array[3][2];
        if( array[3][3] != null )
            log5x5 = array[3][3];
        if( array[3][4] != null )
            log5x6 = array[3][4];

        if( array[4][0] != null )
            log6x2 = array[4][0];
        if( array[4][1] != null )
            log6x3 = array[4][1];
        if( array[4][2] != null )
            log6x4 = array[4][2];
        if( array[4][3] != null )
            log6x5 = array[4][3];
        if( array[4][4] != null )
            log6x6 = array[4][4];

        return array;
    }
}
