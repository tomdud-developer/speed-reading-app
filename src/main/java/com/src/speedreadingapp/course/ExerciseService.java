package com.src.speedreadingapp.course;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Bean
    protected void createExercises(){
        exerciseRepository.save(new Exercise(1L, "test11", 1, 1, 0));
        exerciseRepository.save(new Exercise(2L, "test12", 1, 1, 0));
        exerciseRepository.save(new Exercise(3L, "test13", 1, 1, 0));
        exerciseRepository.save(new Exercise(4L, "test14", 1, 1, 0));
        exerciseRepository.save(new Exercise(5L, "test21", 2, 1, 0));
        exerciseRepository.save(new Exercise(6L, "test22", 2, 1, 0));
        exerciseRepository.save(new Exercise(7L, "test23", 2, 1, 0));
        exerciseRepository.save(new Exercise(8L, "test24", 2, 1, 0));
    }

    List<Exercise> getExercisesWhereSessionNumberEqualsX(Integer x) {
        return exerciseRepository.getExercisesWhereSessionNumberEqualsX(x);
    }
}
