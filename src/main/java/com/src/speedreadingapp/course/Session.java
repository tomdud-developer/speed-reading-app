package com.src.speedreadingapp.course;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Session {
    private int number;
    private final ArrayList<Exercise> session;

    public Session(int number) {
        this.number = number;
        session = new ArrayList<>();
    }

    public void addExerciseToSession(Exercise e) {
        session.add(e);
    }

    public ArrayList<Exercise> getSession() {
        return session;
    }
}
