package com.src.speedreadingapp.jpa.course;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final SessionsGenerator sessionsGenerator;
    private final ExerciseRepository exerciseRepository;
    final int y = 3;
    @Bean
    @Transactional
    protected void createExercises(){
        deleteAll();
        sessionsGenerator.createAll();
    }

    @Transactional
    protected void deleteAll() {
        exerciseRepository.deleteAll();
    }

    List<Exercise> getExercisesWhereSessionNumberEqualsX(Integer x) {
        return exerciseRepository.getExercisesWhereSessionNumberEqualsX(x);
    }
}
