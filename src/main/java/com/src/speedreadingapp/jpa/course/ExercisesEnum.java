package com.src.speedreadingapp.jpa.course;

public enum ExercisesEnum {
        PREDKOSC_CZYTANIA("Prędkość czytania"),
        ZROZUMIENIE_TEKSTU("Zrozumienie tekstu"),
        ZNIKAJACE_LICZBY("Znikające liczby"),
        KOLUMNY_LICZB( "Kolumny liczb"),
        ELIMINACJA_FONETYZACJI("Eliminacja fonetyzacji"),
        SZYTBKIE_SLOWA("Szybkie słowa"),
        TABLICE_SCHULTZA("Tablice Schultza"),
        PIRADMIDA_LICZBOWA("Piramida liczbowa"),
        WSKAZNIK_PODSTAWOWY("Wskaźnik podstawowy"),
        WSKAZNIK_SREDNI("Wskaźnik średni");

        private String exercise;

        ExercisesEnum(String exercise) {
            this.exercise = exercise;
        }

        public String getExercise() {
            return exercise;
        }
}
