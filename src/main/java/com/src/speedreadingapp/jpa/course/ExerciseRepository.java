package com.src.speedreadingapp.jpa.course;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("SELECT e FROM exercises e WHERE e.sessionNumber = ?1")
    List<Exercise> getExercisesWhereSessionNumberEqualsX(Integer x);

}
