package com.src.speedreadingapp.controlers;

import com.src.speedreadingapp.jpa.course.UserProgress;
import com.src.speedreadingapp.jpa.course.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user-progress")
@RequiredArgsConstructor
public class UserProgressController {
    private final UserProgressService userProgressService;

    @GetMapping(value = "/get/{userId}")
    public UserProgress getUserProgress(@PathVariable Long userId) throws Exception {
        return userProgressService.getUserProgressByUserId(userId);
    }


    @PostMapping(value = "/confirm-exercise/{userId}&{exerciseNumber}")
    public ResponseEntity<String> confirmExercise(@PathVariable Long userId, @PathVariable Integer exerciseNumber) throws Exception {
        Integer confirm =  userProgressService.confirmExercise(userId, exerciseNumber);
        if (confirm != exerciseNumber) {
            return new ResponseEntity<>(
                    "There is a problem with saving progress",
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                "Progress saved, exercise confirmed " + confirm,
                HttpStatus.OK);
    }

    @PostMapping(value = "/confirm-session/{userId}&{sessionNumber}")
    public ResponseEntity<String> confirmSession(@PathVariable Long userId, @PathVariable Integer sessionNumber) {
        try {
            Integer confirm =  userProgressService.confirmSession(userId, sessionNumber);
            return new ResponseEntity<>(
                    "Progress saved, new currentSession is " + confirm,
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "There is a problem with saving progress",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/reset-progress/{userId}")
    public ResponseEntity<String> resetProgress(@PathVariable Long userId) {
        try {
            userProgressService.resetProgress(userId);
            return new ResponseEntity<>(
                    "Progress reset completed.",
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "There is a problem with reset progress",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }





}
