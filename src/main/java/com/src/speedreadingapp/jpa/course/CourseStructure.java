package com.src.speedreadingapp.jpa.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseStructure {

    private final ExerciseService exerciseService;

    public ArrayList<Session> getCourseStructure() {
        ArrayList<Session> course = new ArrayList<>();
        int x = 1;
        List<Exercise> listExercisesInSessionX= exerciseService.getExercisesWhereSessionNumberEqualsX(x);
        while(!listExercisesInSessionX.isEmpty()) {
            Session session = new Session(x);
            listExercisesInSessionX.forEach(session::addExerciseToSession);
            course.add(session);
            x++;
            listExercisesInSessionX = exerciseService.getExercisesWhereSessionNumberEqualsX(x);
        }
        return course;
    }


    public Session getSession(Integer number) {
        Session session = new Session(number);
        List<Exercise> listExercisesInSessionX = exerciseService.getExercisesWhereSessionNumberEqualsX(number);
        listExercisesInSessionX.forEach(session::addExerciseToSession);
        return session;
    }
}
