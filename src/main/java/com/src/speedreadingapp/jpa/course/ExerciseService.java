package com.src.speedreadingapp.jpa.course;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    final int y = 3;
    @Bean
    protected void createExercises(){
        createSession1();
        createSession2();
        createSession3();
    }

    private void createSession1() {
        int x = 1;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, "Piramida liczbowa", x, 1, 0, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, "Znikające liczby", x, 2, 1, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, "Tablice Schultza", x, 3, 3, 3));
    }

    private void createSession2() {
        int x = 2;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, "Kolumny Liczb", x, 1, 1, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, "Znikające liczby", x, 2, 2, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, "Szybkie słowa", x, 3, 400, 1));
    }

    private void createSession3() {
        int x = 3;
        exerciseRepository.save(new Exercise((x-1) * y + 1L, "Zrozumienie tekstu", x, 1, 0, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 2L, "Znikające liczby", x, 2, 2, 0));
        exerciseRepository.save(new Exercise((x-1) * y + 3L, "Szybkie słowa", x, 3, 400, 1));
    }

    List<Exercise> getExercisesWhereSessionNumberEqualsX(Integer x) {
        return exerciseRepository.getExercisesWhereSessionNumberEqualsX(x);
    }
}
