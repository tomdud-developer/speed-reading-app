package com.src.speedreadingapp.jpa.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionsGenerator {
    private final ExerciseRepository exerciseRepository;
    final int y = 3;

    public void createAll() {
        createSession1();
        createSession2();
        createSession3();
        createSession4();
        createSession5();
        createSession6();
        createSession7();
        createSession8();
        createSession9();
        createSession10();
        createSession11();
        createSession12();
        createSession13();
        createSession14();
        createSession15();
        createSession16();
        createSession17();
        createSession18();
        createSession19();
        createSession20();
        createSession21();
    }
    private void createSession1() {
        int x = 1;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.PIRADMIDA_LICZBOWA.getExercise(), x, 1, 0, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 2, 1, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 3, 3));
    }

    private void createSession2() {
        int x = 2;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.PREDKOSC_CZYTANIA.getExercise(), x, 1, 1, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 2, 2, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.SZYTBKIE_SLOWA.getExercise(), x, 3, 500, 1));
    }

    private void createSession3() {
        int x = 3;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ZROZUMIENIE_TEKSTU.getExercise(), x, 1, 0, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 2, 2, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 4, 3));
    }

    private void createSession4() {
        int x = 4;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 1, 1000, 6));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.KOLUMNY_LICZB.getExercise(), x, 2, 2, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 4, 3));
    }

    private void createSession5() {
        int x = 5;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 1, 900, 4));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 2, 3, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 4, 4));
    }

    private void createSession6() {
        int x = 6;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 1, 600, 4));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.PIRADMIDA_LICZBOWA.getExercise(), x, 2, 3, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.WSKAZNIK_PODSTAWOWY.getExercise(), x, 3, 40, 0));
    }

    private void createSession7() {
        int x = 7;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 1, 600, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.SZYTBKIE_SLOWA.getExercise(), x, 2, 400, 1));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.WSKAZNIK_PODSTAWOWY.getExercise(), x, 3, 60, 0));
    }

    private void createSession8() {
        int x = 8;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 1, 4, 4));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.SZYTBKIE_SLOWA.getExercise(), x, 2, 200, 1));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.KOLUMNY_LICZB.getExercise(), x, 3, 4, 0));
    }

    private void createSession9() {
        int x = 9;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 1, 5, 4));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.SZYTBKIE_SLOWA.getExercise(), x, 2, 150, 1));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.KOLUMNY_LICZB.getExercise(), x, 3, 5, 0));
    }

    private void createSession10() {
        int x = 10;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.PREDKOSC_CZYTANIA.getExercise(), x, 1, 5, 4));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.SZYTBKIE_SLOWA.getExercise(), x, 2, 400, 2));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.WSKAZNIK_PODSTAWOWY.getExercise(), x, 3, 70, 0));
    }

    private void createSession11() {
        int x = 11;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.KOLUMNY_LICZB.getExercise(), x, 1, 6, 4));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 500, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.WSKAZNIK_SREDNI.getExercise(), x, 3, 30, 0));
    }

    private void createSession12() {
        int x = 12;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 1, 4, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 600, 2));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.WSKAZNIK_PODSTAWOWY.getExercise(), x, 3, 80, 0));
    }

    private void createSession13() {
        int x = 13;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 1, 5, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 400, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.SZYTBKIE_SLOWA.getExercise(), x, 3, 500, 3));
    }

    private void createSession14() {
        int x = 14;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 1, 6, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 350, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 5, 5));
    }

    private void createSession15() {
        int x = 15;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 1, 7, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 300, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 5, 6));
    }

    private void createSession16() {
        int x = 16;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 1, 8, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 300, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 6, 5));
    }

    private void createSession17() {
        int x = 17;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.PIRADMIDA_LICZBOWA.getExercise(), x, 1, 12, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 300, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.KOLUMNY_LICZB.getExercise(), x, 3, 6, 0));
    }

    private void createSession18() {
        int x = 18;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.PIRADMIDA_LICZBOWA.getExercise(), x, 1, 25, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 2, 350, 2));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.ZNIKAJACE_LICZBY.getExercise(), x, 3, 9, 0));
    }

    private void createSession19() {
        int x = 19;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.PIRADMIDA_LICZBOWA.getExercise(), x, 1, 30, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 2, 5, 6));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.WSKAZNIK_SREDNI.getExercise(), x, 3, 50, 0));
    }

    private void createSession20() {
        int x = 20;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.SZYTBKIE_SLOWA.getExercise(), x, 1, 400, 3));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 2, 6, 6));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.ELIMINACJA_FONETYZACJI.getExercise(), x, 3, 300, 2));
    }

    private void createSession21() {
        int x = 21;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, ExercisesEnum.ZROZUMIENIE_TEKSTU.getExercise(), x, 1, 30, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, ExercisesEnum.PREDKOSC_CZYTANIA.getExercise(), x, 2, 5, 5));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, ExercisesEnum.TABLICE_SCHULTZA.getExercise(), x, 3, 6, 6));
    }

}
